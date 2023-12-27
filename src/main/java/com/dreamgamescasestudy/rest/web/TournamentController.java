package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.TournamentCountryScore;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.TournamentService;
import com.dreamgamescasestudy.rest.web.response.CountryLeaderboardResponse;
import com.dreamgamescasestudy.rest.web.response.GroupLeaderboardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tournament")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping("/enter-tournament")
    public List<GroupLeaderboardResponse> EnterTournamentRequest(@RequestParam Long userID) throws InterruptedException {

        tournamentService.enterTournament(userID);

        List<TournamentUserScore> userScores = tournamentService.getUserScoresWithUserId(userID);
        // keep checking until user is matched with a group
        while (userScores == null) {
            Thread.sleep(2000);
            userScores = tournamentService.getUserScoresWithUserId(userID);
        }

        return UserScoresDataToResponse(userScores);
    }

    @GetMapping("/get-rank")
    public int GetGroupRankRequest(@RequestParam Long userID, @RequestParam Long tournamentID) {

        return tournamentService.getGroupRank(userID, tournamentID);
    }

    @GetMapping("/group-leaderboard")
    public List<GroupLeaderboardResponse> GetGroupLeaderboardRequest(@RequestParam Long groupID){

        List<TournamentUserScore> leaderboard =  tournamentService.getGroupLeaderboard((groupID));

        // Putting data into response class using custom function
        return UserScoresDataToResponse(leaderboard);
    }

    @GetMapping("/country-leaderboard")
    public List<CountryLeaderboardResponse> GetCountryLeaderboardRequest(@RequestParam Long tournamentID){

        List<TournamentCountryScore> leaderboard = tournamentService.getCountryLeaderboard(tournamentID);

        // Putting data into response class
        List<CountryLeaderboardResponse> response = new ArrayList<>();
        for (TournamentCountryScore instance : leaderboard){
            CountryLeaderboardResponse newElement = new CountryLeaderboardResponse(instance.getCountry(), instance.getScore());
            response.add(newElement);
        }

        return response;
    }

    @PutMapping("/update-tournament-score")
    public void UpdateTournamentScore(@RequestParam Long userID){
        tournamentService.updateTournamentScore(userID);
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
