package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TournamentService {

    public List<User> enterTournament(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent() && optionalUser.get().getLevel() >= 20 && optionalUser.get().getCoins() >= 1000) {
            return getTournamentGroupUsers(optionalUser.get().getCountry());
        } else {
            throw new CustomException("Invalid request to enter the tournament");
        }
    }

    public List<User> getTournamentGroupUsers(String country) {
        return List.of();
    }

    public List<User> claimTournamentReward(Long userId) {
        return getTournamentGroupUsers(userRepository.findById(userId).orElseThrow().getCountry());
    }

    public List<Tournament> getGroupLeaderboard(Long groupId) {
        return List.of();
    }

    public List<Leaderboard> getCountryLeaderboard() {
        return List.of();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void startNewTournament() {
        Tournament newTournament = new Tournament();
        newTournament.setTimestamp(LocalDateTime.now());
        tournamentRepository.save(newTournament);
    }

    @Scheduled(cron = "0 0 20 * * ?") // Daily at 20:00 (UTC)
    public void closeTournament() {
        // Logic to close the ongoing tournament
    }
}
