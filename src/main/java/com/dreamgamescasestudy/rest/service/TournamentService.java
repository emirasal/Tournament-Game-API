package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final TournamentGroupRepository tournamentGroupRepository;

    private final TournamentCountryScoreRepository tournamentCountryScoreRepository;
    private final TournamentUserScoreRepository tournamentUserScoreRepository;

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

        // Finding all the groups of this tournament
        List<TournamentGroup> groups = tournamentGroupRepository.findByTournament(currentTournament);

        // Distributing coins to tournament group winners
        giveRewardsToGroups(groups);

        // Updating tournament's status
        currentTournament.setTournamentState(TournamentState.INACTIVE);
        tournamentRepository.save(currentTournament);
    }

    private void giveRewardsToGroups (List<TournamentGroup> groups) {
        for (TournamentGroup group : groups) {
            List<TournamentUserScore> leaderboard = tournamentUserScoreRepository.findByTournamentGroup(group);

            // sorting by the score and to get first and second places
            leaderboard.sort(Comparator.comparingInt(TournamentUserScore::getScore).reversed());

            User first = leaderboard.get(0).getUser();
            first.setPendingCoins(1000);
            userRepository.save(first);

            User second = leaderboard.get(1).getUser();
            second.setPendingCoins(5000);
            userRepository.save(second);
        }
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
                    TournamentUserScore newParticipant = TournamentUserScore.builder().tournamentGroup(newGroup).user(user).build();
                    tournamentUserScoreRepository.save(newParticipant);

                    userQueue.remove(user);

                    // We also add this person's country added to country scores
                    TournamentCountryScore newCountryInstance = TournamentCountryScore.builder().country(user.getCountry()).tournament(currentTournament).build();
                    tournamentCountryScoreRepository.save(newCountryInstance);
                }
            }
        }
    }

    public List<TournamentUserScore> checkUserScoresWithUserId(Long userID){
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Finding the groups of the current tournament
            List<TournamentGroup> groups = tournamentGroupRepository.findByTournament(currentTournament);

            // Finding the correct tournamentUserScore instance (pair of user, group)
            TournamentUserScore tournamentUserScore = tournamentUserScoreRepository.findByUserAndTournamentGroupIn(user, groups);

            if (tournamentUserScore != null) {
                return getGroupLeaderboard(tournamentUserScore.getTournamentGroup().getGroupID());
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

    public int getGroupRank(Long userID, Long tournamentID){
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
            TournamentGroup group = tournamentUserScoreRepository.findByUserAndTournamentGroupIn(user, groups).getTournamentGroup();

            // Finding the users for that groupID (5 users)
            List<TournamentUserScore> leaderboard = tournamentUserScoreRepository.findByTournamentGroup(group);

            // Sorting the list
            leaderboard.sort(Comparator.comparingInt(TournamentUserScore::getScore).reversed());

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
    
    
    public List<TournamentUserScore> getGroupLeaderboard(Long groupID){
        Optional<TournamentGroup> optionalGroup = tournamentGroupRepository.findById(groupID);

        if (optionalGroup.isPresent()) {
            TournamentGroup group = optionalGroup.get();
            List<TournamentUserScore> leaderboard = tournamentUserScoreRepository.findByTournamentGroup(group);

            // Sorting the list
            leaderboard.sort(Comparator.comparingInt(TournamentUserScore::getScore).reversed());

            return leaderboard;
        }
        // No such group
        return null;
    }

    public List<TournamentCountryScore> getCountryLeaderboard(Long tournamentID) {

        Optional<Tournament> optionalTournament = tournamentRepository.findById(tournamentID);

        if(optionalTournament.isPresent()) {
            Tournament tournament = optionalTournament.get();
            List<TournamentCountryScore> tournamentCountryScores = tournamentCountryScoreRepository.findByTournament(tournament);

            // Sorting in terms of score
            tournamentCountryScores.sort(Comparator.comparing(TournamentCountryScore::getScore).reversed());

            return tournamentCountryScores;
        }
        // Tournament does not exist
        return null;
    }

    public void updateTournamentScore(long userID){
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Finding the groups belonging to the current tournament
            List<TournamentGroup> groups = tournamentGroupRepository.findByTournament(currentTournament);

            // Finding the row
            TournamentUserScore tournamentUserScoreInstance = tournamentUserScoreRepository.findByUserAndTournamentGroupIn(user, groups);
            tournamentUserScoreInstance.setScore(tournamentUserScoreInstance.getScore() + 1);
            tournamentUserScoreRepository.save(tournamentUserScoreInstance);

            // Also updating user's country's score
            // Finding the correct (tournamentID, country match)
            TournamentCountryScore tournamentCountryScoreInstance = tournamentCountryScoreRepository.findByTournamentAndCountry(currentTournament, user.getCountry());
            tournamentCountryScoreInstance.setScore(tournamentCountryScoreInstance.getScore() + 1);
            tournamentCountryScoreRepository.save(tournamentCountryScoreInstance);
        }
    }
}
