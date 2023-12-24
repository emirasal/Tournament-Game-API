package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.*;

import org.slf4j.Logger;




@Service
@Transactional
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentGroupRepository tournamentGroupRepository;

    private final CountryLeaderboardRepository countryLeaderboardRepository;
    private final GroupLeaderboardRepository groupLeaderboardRepository;

    private final UserRepository userRepository;

    Tournament currentTournament;
    private Queue<User> userQueue; // For matching the users to a group


    //@Scheduled(cron = "0 0 0 * * *", zone = "UTC") // Daily at 00:00 (UTC)
    @PostConstruct
    public void startNewTournament() {
        currentTournament = new Tournament();
        tournamentRepository.save(currentTournament);

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
            Map<Country, User> countryUserMap = new HashMap<>(); // To store the unique Country / User pairs

            for (User  currentUser : userQueue){
                if (countryUserMap.size() >= 5) {
                    break; // Exiting if 5 unique countries are found
                }

                if (!countryUserMap.containsKey(currentUser.getCountry())) {
                    countryUserMap.put(currentUser.getCountry(), currentUser);

                }
            }

            if (countryUserMap.size() == 5) { // We can form the group
                // Creating the tournament group
                TournamentGroup newGroup = TournamentGroup.builder().tournament(currentTournament).build();
                tournamentGroupRepository.save(newGroup);
                for (User user : countryUserMap.values()) {
                    // Creating 5 instance for groupLeaderboards with having the same group
                    GroupLeaderboard newParticipant = GroupLeaderboard.builder().tournamentGroup(newGroup).user(user).build();
                    userQueue.remove(user);
                    groupLeaderboardRepository.save(newParticipant);

                }
            }
        }
    }

    public List<GroupLeaderboard> checkGroupLeaderboard(Long userID){
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<GroupLeaderboard> optionalGroupLeaderboard = Optional.ofNullable(groupLeaderboardRepository.findByUser(user));

            if (optionalGroupLeaderboard.isPresent()) {
                GroupLeaderboard leaderboardInstance = optionalGroupLeaderboard.get();
                return GetGroupLeaderboard(leaderboardInstance.getTournamentGroup().getGroupID());
            }
        }
        return null;
    }

    public void enterTournament(Long userID) {

        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPendingCoins() == 0) {
                /*
                if (user.getCoins() >= 1000) {
                user.getLevel() >= 20
                }
                user.setCoins(user.getCoins() - 1000); */

                userQueue.offer(user);
                formGroups();

                // Leaderboard will be returned from the checkGroupLeaderboard method
            }
        }
    }

    public int GetGroupRank(Long userID, Long tournamentID){
        // Finding the tournament by the id
        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentID);
        // Finding the user by the id
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalTournament.isPresent() && optionalUser.isPresent()) {
            Tournament tournament = optionalTournament.get();
            User user = optionalUser.get();

            // Getting the groups of that tournament
            List<TournamentGroup> groups = tournamentGroupRepository.findByTournament(tournament);

            // Finding the correct group alongside a list of groups (pair of user & group match)
            TournamentGroup group = groupLeaderboardRepository.findByUserAndTournamentGroupIn(user, groups).getTournamentGroup();

            // Finding the users for that groupID (5 users)
            List<GroupLeaderboard> leaderboard = groupLeaderboardRepository.findByTournamentGroup(group);

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
        // tournamentID or userID is not valid
       return -1;
    }
    
    
    public List<GroupLeaderboard> GetGroupLeaderboard(Long groupID){
        Optional<TournamentGroup> optionalGroup = tournamentGroupRepository.findById(groupID);

        if (optionalGroup.isPresent()) {
            TournamentGroup group = optionalGroup.get();
            List<GroupLeaderboard> leaderboard = groupLeaderboardRepository.findByTournamentGroup(group);

            // Sorting the list
            leaderboard.sort(Comparator.comparingInt(GroupLeaderboard::getUserScore).reversed());

            return leaderboard;
        }
        // No such group
        return null;
    }

    public List<CountryLeaderboard> GetCountryLeaderboard(Long tournamentID) {

        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentID);

        if(optionalTournament.isPresent()) {
            Tournament tournament = optionalTournament.get();
            List<CountryLeaderboard> countryLeaderboard = countryLeaderboardRepository.findByTournament(tournament);

            // Sorting in terms of score
            countryLeaderboard.sort(Comparator.comparing(CountryLeaderboard::getScore).reversed());

            return countryLeaderboard;
        }
        // Tournament does not exist
        return null;
    }

}
