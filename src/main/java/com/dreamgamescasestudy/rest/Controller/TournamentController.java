package com.dreamgamescasestudy.rest.Controller;

import com.dreamgamescasestudy.rest.Model.User;
import com.dreamgamescasestudy.rest.Repo.UserRepo;
import com.dreamgamescasestudy.rest.Tournament.Player;
import com.dreamgamescasestudy.rest.Tournament.SortedPlayerSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TournamentController {

    @Autowired
    private UserRepo userRepository;

    private SortedPlayerSet GroupLeaderboard;
    private SortedPlayerSet CountryLeaderboard;


    @Scheduled(cron = "0 0 0 * * ?") // Daily at 00:00 (UTC)
    public void openTournament() {
        GroupLeaderboard = new SortedPlayerSet();
        CountryLeaderboard = new SortedPlayerSet();
    }

    @Scheduled(cron = "0 0 20 * * ?") // Daily at 20:00 (UTC)
    public void closeTournament() {

    }





    @PostMapping(value = "/tournament/join/{userID}")
    public SortedPlayerSet EnterTournamentRequest(@PathVariable long userID) {
        User user = userRepository.findById(userID).get();
        Player currentPlayer = new Player(user);

        GroupLeaderboard.add(currentPlayer);


        return GroupLeaderboard;
    }
}
