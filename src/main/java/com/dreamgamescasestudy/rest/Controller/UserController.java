package com.dreamgamescasestudy.rest.Controller;

import com.dreamgamescasestudy.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(value = "/user/create/{username}")
    public User CreateUserRequest(@PathVariable String username) {
        User newUser = new User(username);
        userRepository.save(newUser);
        return newUser;
    }

    @PutMapping(value = "/user/updateLevel/{userID}")
    public User UpdateLevelRequest(@PathVariable long userID){
        User updatedUser = userRepository.findById(userID).get();
        updatedUser.setCoins(updatedUser.getCoins() + 25);
        updatedUser.setLevel((updatedUser.getLevel() + 1));
        return updatedUser;
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable long id, @RequestBody User userToBeDeleted){
        return "asd";
    }

}
