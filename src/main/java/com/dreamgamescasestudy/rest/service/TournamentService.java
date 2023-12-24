package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.repository.*;
import com.dreamgamescasestudy.rest.web.response.GroupLeaderboardResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentGroupRepository tournamentGroupRepository;

    private final CountryLeaderboardRepository countryLeaderboardRepository;
    private final GroupLeaderboardRepository groupLeaderboardRepository;

    private final UserRepository userRepository;

    private Queue<User> userQueue; // For matching the users to a group

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC") // Daily at 00:00 (UTC)
    public void startNewTournament() {
        Tournament newTournament = new Tournament();
        tournamentRepository.save(newTournament);

        userQueue = new LinkedList<>();
    }

    @Scheduled(cron = "0 0 20 * * ?") // Daily at 20:00 (UTC)
    public void closeTournament() {
        // Distributing coins to winners

        // Optional<TournamentGroup> optionalTournamentGroup = tournamentGroupRepository.findById(UUID.randomUUID());
        // optionalTournamentGroup.ifPresent(optionalTournamentGroup -> optionalTournamentGroup.getGroupLeaderboards());


        Tournament activeTournament = tournamentRepository.findByTournamentState(TournamentState.ACTIVE);
        activeTournament.setTournamentState(TournamentState.INACTIVE);
        tournamentRepository.save(activeTournament);

    }


    private void formGroups(){
        if (userQueue.size() >= 5) {
            Map<Country, User> countryUserMap = new HashMap<>();

            for (User currentUser : userQueue) {
                if (countryUserMap.size() >= 5) {
                    break; // Exiting if 5 unique countries are found
                }

                if (!countryUserMap.containsKey(currentUser.getCountry())) {
                    countryUserMap.put(currentUser.getCountry(), currentUser);

                }
            }

            if (countryUserMap.size() > 5) {
                // Creating the tournament group
                TournamentGroup newGroup = TournamentGroup.builder().build();
                tournamentGroupRepository.save(newGroup);
                for (User user : countryUserMap.values()) {
                    // Creating 5 instance for groupLeaderboards with the same group
                    groupLeaderboardRepository.save(GroupLeaderboard.builder().tournamentGroup(newGroup).username(user.getUsername()).user(user).build());
                }
            }
        }
    }

    public List<GroupLeaderboard> EnterTournament(Long userID) throws InterruptedException {

        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Checking if user cannot enter
            if (user.getPendingCoins() > 0 && user.getLevel() < 20) {
                // not enough level or previous reward not claimed
                return null;
            }
            if (user.getCoins() < 1000) {
                // not enough money to enter
                return null;
            }
            user.setCoins(user.getCoins() - 1000);

            userQueue.offer(user);
            formGroups();

            // Assume we've formed the group and search the person's group (2 seconds intervals)
            Long groupID;
            do {
                Thread.sleep(2000);
                groupID = groupLeaderboardRepository.findGroupIdByUserId(user.getUserID());
            }
            while(groupID == null);

            return groupLeaderboardRepository.findByGroupId(groupID);

        }
        // User does not exist!
        return null;
    }

    public int GetGroupRank(Long userID, Long tournamentID){
        // Getting the groupIDs of that tournament
        List<Long> groupIDs = tournamentGroupRepository.findGroupIdsByTournamentId(tournamentID);

        // Finding the groupID matching that user
        Long groupID = groupLeaderboardRepository.findGroupIdByUserIdAndGroupId(userID, tournamentID);

        // Finding the users for that groupID (5 users)
        List<GroupLeaderboard> leaderboard = groupLeaderboardRepository.findByGroupId(groupID);

        // Sorting the list
        leaderboard.sort(Comparator.comparingInt(GroupLeaderboard::getUserScore).reversed());

        // Finding the user's rank
        int rank = 0;

        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).getUser().getUserID().equals(userID)) {
                rank = i + 1; // Adding 1 to start rank from 1 instead of 0
                break;
            }
        }

        return rank;
    }
    
    
    public List<GroupLeaderboard> GetGroupLeaderboard(Long groupID){

        List<GroupLeaderboard> leaderboard = groupLeaderboardRepository.findByGroupId(groupID);

        // Sorting the list
        leaderboard.sort(Comparator.comparingInt(GroupLeaderboard::getUserScore).reversed());

        return leaderboard;
    }

    public List<CountryLeaderboard> GetCountryLeaderboard(Long tournamentID) {
        return countryLeaderboardRepository.findByTournamentTournamentId(tournamentID);
    }

}
