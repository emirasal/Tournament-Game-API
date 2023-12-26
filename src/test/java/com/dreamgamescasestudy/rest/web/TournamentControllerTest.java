package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.Country;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.TournamentService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TournamentService tournamentService;

    @Test
    void enterTournamentRequest() throws Exception {

        tournamentService.startNewTournament();

        // Sending 4 different requests first
        Country[] countries = Country.values();
        for (int i=0; i < countries.length-1; i++) {
            User testUser = User.builder().level(20).country(countries[i]).build();
            tournamentService.enterTournament(testUser.getUserID());
        }

       // We create the last user and check if they formed a group
        User testUser = User.builder().level(20).country(countries[countries.length-1]).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/tournament/enter-tournament")
                        .param("userID", String.valueOf(testUser.getUserID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getGroupRankRequest() throws Exception {
        tournamentService.startNewTournament();
        // We need 5 people which already formed a group
        User testUser = null;
        for (Country country : Country.values()) {
            testUser = User.builder().level(20).country(country).build();
            tournamentService.enterTournament(testUser.getUserID());
        }

        // Testing for the last user
        assert testUser != null;
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tournament/get-rank")
                        .param("userID", String.valueOf(testUser.getUserID()))
                        .param("tournamentID", String.valueOf(tournamentService.getCurrentTournament().getTournamentID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").isNumber())
                .andExpect(jsonPath("$").value(Matchers.greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$").value(Matchers.lessThanOrEqualTo(5)));
    }

    @Test
    void getGroupLeaderboardRequest() throws Exception {
        tournamentService.startNewTournament();

        // We need 5 people which already formed a group
        User testUser = null;
        for (Country country : Country.values()) {
            testUser = User.builder().level(20).country(country).build();
            tournamentService.enterTournament(testUser.getUserID());
        }

        // Finding the group from one of the users
        assert testUser != null;
        List<TournamentUserScore> result = new ArrayList<>();
        when(tournamentService.getUserScoresWithUserId(testUser.getUserID())).thenReturn(result);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tournament/get-group-leaderboard")
                        .param("groupID", String.valueOf(result.get(0).getTournamentGroup().getGroupID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());

    }

    @Test
    void getCountryLeaderboardRequest() throws Exception {
        tournamentService.startNewTournament();

        // We need 5 people which already formed a group
        User testUser = null;
        for (Country country : Country.values()) {
            testUser = User.builder().level(20).country(country).build();
            tournamentService.enterTournament(testUser.getUserID());
        }

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/tournament/get-country-leaderboard")
                        .param("tournamentID", String.valueOf(tournamentService.getCurrentTournament().getTournamentID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    void updateTournamentScore() throws Exception {
        tournamentService.startNewTournament();

        // We need 5 people which already formed a group
        User testUser = null;
        for (Country country : Country.values()) {
            testUser = User.builder().level(20).country(country).build();
            tournamentService.enterTournament(testUser.getUserID());
        }

        assert testUser != null;
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tournament/update-tournament-score")
                        .param("userID", String.valueOf(testUser.getUserID())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}