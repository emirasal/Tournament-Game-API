package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.TournamentCountryScore;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.TournamentService;
import com.dreamgamescasestudy.rest.web.response.CountryLeaderboardResponse;
import com.dreamgamescasestudy.rest.web.response.GroupLeaderboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping("/enter-tournament")
    public ResponseEntity<?> EnterTournamentRequest(@RequestParam Long userID) throws InterruptedException {

        // Will put the user in queue
        ErrorMessage message = tournamentService.enterTournament(userID);

        String errorMessage = switch (message) {
            case USER_DOES_NOT_EXIST -> "No such user";
            case NOT_ENOUGH_LEVEL -> "Not enough level";
            case PENDING_COINS_EXIST -> "Claim the previous rewards before entering";
            case NOT_ENOUGH_COINS -> "You must have at least 1000 coins";
            case OK -> null;
        };

        if (message != ErrorMessage.OK) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

        // Everything is fine
        List<TournamentUserScore> userScores = tournamentService.getUserScoresWithUserId(userID);
        // keep checking until user is matched with a group
        while (userScores == null) {
            Thread.sleep(2000);
            userScores = tournamentService.getUserScoresWithUserId(userID);
        }

        return ResponseEntity.ok(UserScoresDataToResponse(userScores));
    }

    @GetMapping("/get-rank")
    public ResponseEntity<?> getGroupRankRequest(@RequestParam Long userID, @RequestParam Long tournamentID) {
        int rank = tournamentService.getGroupRank(userID, tournamentID);
        if (rank == -1) {
            String errorMessage = "Failed to retrieve rank for userID: " + userID + " in tournamentID: " + tournamentID;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

        }
        return ResponseEntity.ok(rank);
    }

    @GetMapping("/group-leaderboard")
    public ResponseEntity<?> getGroupLeaderboardRequest(@RequestParam Long groupID) {
        List<TournamentUserScore> leaderboard = tournamentService.getGroupLeaderboard(groupID);
        if (leaderboard == null) {
            String errorMessage = "No leaderboard data available for groupID: " + groupID;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        List<GroupLeaderboardResponse> response = UserScoresDataToResponse(leaderboard);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/country-leaderboard")
    public ResponseEntity<?> getCountryLeaderboardRequest(@RequestParam Long tournamentID) {
        List<TournamentCountryScore> leaderboard = tournamentService.getCountryLeaderboard(tournamentID);
        if (leaderboard == null || leaderboard.isEmpty()) {
            String errorMessage = "No leaderboard data available for tournamentID: " + tournamentID;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }

        List<CountryLeaderboardResponse> response = new ArrayList<>();
        for (TournamentCountryScore instance : leaderboard){
            CountryLeaderboardResponse newElement = new CountryLeaderboardResponse(instance.getCountry(), instance.getScore());
            response.add(newElement);
        }

        return ResponseEntity.ok(response);
    }
    @PutMapping("/update-tournament-score")
    public ResponseEntity<?> updateTournamentScore(@RequestParam Long userID) {
        int score = tournamentService.updateTournamentScore(userID);
        if (score == -1) {
            String errorMessage = "Failed to update tournament score for userID: " + userID;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
        return ResponseEntity.ok(score);
    }


    private List<GroupLeaderboardResponse> UserScoresDataToResponse(List<TournamentUserScore> userScores) {
        List<GroupLeaderboardResponse> response = new ArrayList<>();
        for (TournamentUserScore instance : userScores){
            User userData = instance.getUser();
            GroupLeaderboardResponse newElement = new GroupLeaderboardResponse(userData.getUserID(), userData.getUsername(), userData.getCountry(), instance.getScore());
            response.add(newElement);
        }
        return response;
    }
}