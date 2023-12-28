package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.service.TournamentService;
import com.dreamgamescasestudy.rest.service.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
        when(userService.createUser("testUser")).thenReturn(User.builder().userID(1L).username("testUser").level(1).coins(5000).country(Country.TURKEY).build());

        mockMvc.perform(post("/api/v1/user/create-user")
                        .param("username", "testUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.coins").value(5000))
                .andExpect(jsonPath("$.country").value("TURKEY"));

        verify(userService, times(1)).createUser("testUser");
    }

    @Test
    void testUpdateUserLevelRequest() throws Exception {
        when(userService.updateUserLevel(1L)).thenReturn(User.builder().userID(1L).username("testUser").level(2).coins(5000).country(Country.TURKEY).build());

        mockMvc.perform(put("/api/v1/user/update-level")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(2))
                .andExpect(jsonPath("$.coins").value(5000))
                .andExpect(jsonPath("$.country").value("TURKEY"));

        verify(userService, times(1)).updateUserLevel(1L);
    }

    @Test
    void testClaimRewardRequest() throws Exception {
        when(userService.claimTournamentReward(1L)).thenReturn(User.builder().userID(1L).username("testUser").level(1).coins(5500).country(Country.TURKEY).build());

        mockMvc.perform(put("/api/v1/user/claim-rewards")
                        .param("userID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.level").value(1))
                .andExpect(jsonPath("$.country").value("TURKEY"))
                .andExpect(jsonPath("$.coins").value(greaterThan(5000)));

        verify(userService, times(1)).claimTournamentReward(1L);
    }
}