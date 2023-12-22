package com.dreamgamescasestudy.rest.Controller;

import com.dreamgamescasestudy.rest.Model.User;
import com.dreamgamescasestudy.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserRepo userRepository;

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
