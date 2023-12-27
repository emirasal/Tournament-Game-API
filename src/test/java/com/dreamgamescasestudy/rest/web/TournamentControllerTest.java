package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.service.TournamentService;
import com.dreamgamescasestudy.rest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@WebMvcTest(TournamentController.class)
class TournamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TournamentService tournamentService;

    @Mock
    TournamentController tournamentController;

    @BeforeEach
    void setUp() {
        tournamentController = new TournamentController(tournamentService); // Initialize the controller with the mocked service
        mockMvc = MockMvcBuilders.standaloneSetup(tournamentController).build(); // Set up MockMvc with the controller
    }

    @Test
    void testGetGroupRankRequest() throws Exception {
        // Mock the behavior of tournamentService.getGroupRank(userID, tournamentID)
        when(tournamentService.getGroupRank(anyLong(), anyLong())).thenReturn(5); // Mocking the rank to be 5

        // Perform MockMvc request
        mockMvc.perform(get("/api/v1/tournament/get-rank")
                        .param("userID", "1")
                        .param("tournamentID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Assuming JSON response
                .andExpect(jsonPath("$").value(5)); // Assuming the response structure returns the rank directly

        // Verify that tournamentService.getGroupRank was called with the correct parameters
        verify(tournamentService, times(1)).getGroupRank(1L, 1L);

    }

    @Test
    void testGetGroupLeaderboardRequest() throws Exception {
        // Create mock TournamentUserScore objects
        List<TournamentUserScore> mockLeaderboard = Arrays.asList(
                TournamentUserScore.builder().tournamentUserId(1L).score(100).build(),
                TournamentUserScore.builder().tournamentUserId(2L).score(90).build()
                // Add more mock data as needed
        );

        // Mock the behavior of tournamentService.getGroupLeaderboard(groupID)
        when(tournamentService.getGroupLeaderboard(anyLong())).thenReturn(mockLeaderboard);

        // Perform MockMvc request
        mockMvc.perform(get("/api/v1/tournament/group-leaderboard")
                        .param("groupID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Assuming JSON response
                .andExpect(jsonPath("$[0].tournamentUserId").value(1L)) // Assuming the response structure
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[1].tournamentUserId").value(2L))
                .andExpect(jsonPath("$[1].score").value(90));

        // Verify that tournamentService.getGroupLeaderboard was called with the correct parameter
        verify(tournamentService, times(1)).getGroupLeaderboard(1L);
    }

    @Test
    void testGetCountryLeaderboardRequest() throws Exception {
        // Create mock TournamentCountryScore objects
        List<TournamentCountryScore> mockLeaderboard = Arrays.asList(
                TournamentCountryScore.builder().country(Country.TURKEY).score(100).build(),
                TournamentCountryScore.builder().country(Country.GERMANY).score(90).build()
                // Add more mock data as needed
        );

        // Mock the behavior of tournamentService.getCountryLeaderboard(tournamentID)
        when(tournamentService.getCountryLeaderboard(anyLong())).thenReturn(mockLeaderboard);

        // Perform MockMvc request
        mockMvc.perform(get("/api/v1/tournament/country-leaderboard")
                        .param("tournamentID", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Assuming JSON response
                .andExpect(jsonPath("$[0].country").value("TURKEY")) // Assuming the response structure
                .andExpect(jsonPath("$[0].score").value(100))
                .andExpect(jsonPath("$[1].country").value("GERMANY"))
                .andExpect(jsonPath("$[1].score").value(90));

        // Verify that tournamentService.getCountryLeaderboard was called with the correct parameter
        verify(tournamentService, times(1)).getCountryLeaderboard(1L);
    }

    @Test
    void testUpdateTournamentScore() throws Exception {
        // Perform MockMvc request
        mockMvc.perform(put("/api/v1/tournament/update-tournament-score")
                        .param("userID", "1"))
                .andExpect(status().isOk());

        // Verify that tournamentService.updateTournamentScore was called with the correct parameter
        verify(tournamentService, times(1)).updateTournamentScore(1L);
    }

}