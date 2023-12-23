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

    public List<GroupLeaderboardResponse> EnterTournament(Long userID){

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


            // Checking the last group for available slot
            // If not form a new Group
            Optional<TournamentGroup> OptionalLastGroup = tournamentGroupRepository.findTopByOrderByGroupIdDesc();
            if (OptionalLastGroup.isPresent()) {
                TournamentGroup lastGroup = OptionalLastGroup.get();
                Long lastGroupID = lastGroup.getGroupID();

                // We have the last group now find the users in that group
                List<GroupLeaderboard> lastGroupUsers = groupLeaderboardRepository.findByGroupId(lastGroupID);

                // Check if there exist a person with the same country in that group
                for (GroupLeaderboard userInGroup : lastGroupUsers) {
                    if (userInGroup.getUser().getCountry() == user.getCountry()) {
                        // Cannot enter here so we will form a new group for that person

                    }
                    
                }

            } else {
                // This is the first group

            }

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
