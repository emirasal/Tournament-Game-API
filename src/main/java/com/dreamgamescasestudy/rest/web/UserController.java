package com.dreamgamescasestudy.rest.web;

import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest){
        User user = userService.createUser(createUserRequest.username());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}/update-level")
    public ResponseEntity<Optional<User>> updateUserLevel(@PathVariable long userId){
        Optional<User> optionalUser = userService.updateUserLevel(userId);
        return new ResponseEntity<>(optionalUser, HttpStatus.CREATED);
    }
}
