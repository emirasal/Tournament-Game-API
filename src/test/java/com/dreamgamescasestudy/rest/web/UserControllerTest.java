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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.greaterThan;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    TournamentService tournamentService;

    @Test
    void testCreateUserRequest() throws Exception {
        // Mock userService.createUser method
        when(userService.createUser("testUser")).thenReturn(User.builder().userID(1L).username("testUser").level(1).coins(5000).country(Country.TURKEY).build());

        // Perform MockMvc request
        mockMvc.perform(post("/api/v1/user/create-user")
                        .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.coins").value(5000))
                .andExpect(jsonPath("$.country").value("TURKEY"));

        // Verify that userService.createUser was called with the correct parameter
        verify(userService, times(1)).createUser("testUser");
    }

    @Test
    void testUpdateUserLevelRequest() throws Exception {
        // Mock userService.updateUserLevel method
        when(userService.updateUserLevel(1L)).thenReturn(User.builder().userID(1L).username("testUser").level(2).coins(5000).country(Country.TURKEY).build());

        // Perform MockMvc request
        mockMvc.perform(put("/api/v1/user/update-level")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(2))
                .andExpect(jsonPath("$.coins").value(5000))
                .andExpect(jsonPath("$.country").value("TURKEY"));

        // Verify that userService.updateUserLevel was called with the correct parameter
        verify(userService, times(1)).updateUserLevel(1L);
    }

    @Test
    void testClaimRewardRequest() throws Exception {
        // Mock userService.ClaimTournamentReward method
        when(userService.ClaimTournamentReward(1L)).thenReturn(User.builder().userID(1L).username("testUser").level(1).coins(5500).country(Country.TURKEY).build());

        // Perform MockMvc request
        mockMvc.perform(put("/api/v1/user/claim-rewards")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.country").value("TURKEY"))
                .andExpect(jsonPath("$.coins").value(greaterThan(5000)));

        // Verify that userService.ClaimTournamentReward was called with the correct parameter
        verify(userService, times(1)).ClaimTournamentReward(1L);
    }



}