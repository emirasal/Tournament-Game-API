package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.UserService;
import com.dreamgamescasestudy.rest.web.response.UserProgressResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create-user")
    public UserProgressResponse createUserRequest(@RequestParam String username){
        User user = userService.createUser(username);
        return new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry());
    }

    @PutMapping("/{userID}/update-level")
    public UserProgressResponse updateUserLevelReqeust(@PathVariable long userID){
        User user = userService.updateUserLevel(userID);
        return new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry());
    }

    @PutMapping("/{userID}/claim-rewards")
    public UserProgressResponse ClaimRewardRequest(@PathVariable long userID){
        User user = userService.ClaimTournamentReward(userID);
        return new UserProgressResponse(user.getUserID(), user.getLevel(), user.getCoins(), user.getCountry());
    }

}
