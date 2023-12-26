package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.repository.UserRepository;
import com.dreamgamescasestudy.rest.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void createUserRequest() throws Exception {
        User testUser = User.builder().build();
        when(userService.createUser("testUser")).thenReturn(testUser);

        // Perform POST request to createUserRequest endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/create-user")
                        .param("username", "testUser"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.level").value(testUser.getLevel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coins").value(testUser.getCoins()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(testUser.getCountry().toString()));
    }


    @Test
    void updateUserLevelRequest() throws Exception {

        User testUser = User.builder().build();

        // Saving the user to the database
        when(userService.createUser("testUser")).thenReturn(testUser);

        // Perform POST request to createUserRequest endpoint to create the user
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/create-user")
                        .param("username", "testUser"))
                .andExpect(MockMvcResultMatchers.status().isOk());


        // Mock the update of user's level
        when(userService.updateUserLevel(testUser.getUserID())).thenReturn(testUser);

        // Perform PUT request to updateUserLevelRequest endpoint to update the user's level
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/update-level")
                        .param("userID", String.valueOf(testUser.getUserID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.level").value(2)); // Assuming the level incremented by 1
    }


    @Test
    void claimRewardsRequest() throws Exception {

        // Assuming we have a user that have coins to claim
        User testUser = User.builder().pendingCoins(10000).build();
        when(userService.createUser("testUser")).thenReturn(testUser);

        // Mocking the user's existence in the database
        when(userService.ClaimTournamentReward(testUser.getUserID())).thenReturn(testUser);

        // Performing the PUT request to claim rewards
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/claim-rewards")
                        .param("userID", String.valueOf(testUser.getUserID())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userID").value(testUser.getUserID()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.coins").value(testUser.getCoins() + testUser.getPendingCoins()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pendingCoins").value(0)); // Assuming pending coins get claimed
    }

}