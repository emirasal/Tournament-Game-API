package com.dreamgamescasestudy.rest.Controller;

import com.dreamgamescasestudy.rest.repository.UserRepository;
import com.dreamgamescasestudy.rest.Tournament.Player;
import com.dreamgamescasestudy.rest.Tournament.SortedPlayerSet;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TournamentController {

    private final UserRepository userRepository;

    private SortedPlayerSet GroupLeaderboard;
    private SortedPlayerSet CountryLeaderboard;


    @Scheduled(cron = "0 0 0 * * ?") // Daily at 00:00 (UTC)
    public void openTournament() {
        GroupLeaderboard = new SortedPlayerSet(); // Empty at the beginning
        CountryLeaderboard = new SortedPlayerSet(); // 5 Country with score 0
    }
    @Scheduled(cron = "0 0 20 * * ?") // Daily at 20:00 (UTC)
    public void closeTournament() {

    }




    @PostMapping(value = "/tournament/join/{userID}")
    public SortedPlayerSet EnterTournamentRequest(@PathVariable long userID) {
        User user = userRepository.findById(userID).get();
        Player currentPlayer = new Player(user); // Assigning some fields to play class
        GroupLeaderboard.add(currentPlayer);


        return GroupLeaderboard;
    }
}
