package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.UserService;
import com.dreamgamescasestudy.rest.web.response.UserProgressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUserRequest (@RequestParam String username){
        User user = userService.createUser(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating the user!");
        }
        return ResponseEntity.ok( new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry()));
    }

    @PutMapping("/update-level")
    public ResponseEntity<?> updateUserLevelRequest(@RequestParam long userID) {
        User user = userService.updateUserLevel(userID);
        if (user == null) {
            String errorMessage = "Failed to update user level for userID: " + userID;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }

        return ResponseEntity.ok(new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry()));
    }

    @PutMapping("/claim-rewards")
    public ResponseEntity<?> claimRewardRequest(@RequestParam long userID) {
        User user = userService.claimTournamentReward(userID);
        if (user == null) {
            String errorMessage = "Failed to claim rewards for userID: " + userID;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
        return ResponseEntity.ok(new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry()));
    }
}