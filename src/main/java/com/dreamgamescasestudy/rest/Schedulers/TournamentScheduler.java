package com.dreamgamescasestudy.rest.Schedulers;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TournamentScheduler {
    
    @Scheduled(cron = "0 0 0 * * ?") // Daily at 00:00 (UTC)
    public void openTournament() {
        // Logic to start a new tournament
    }

    @Scheduled(cron = "0 0 20 * * ?") // Daily at 20:00 (UTC)
    public void closeTournament() {
        // Logic to close the ongoing tournament
    }


}
