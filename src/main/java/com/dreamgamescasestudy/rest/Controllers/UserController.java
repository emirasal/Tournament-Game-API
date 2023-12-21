package com.dreamgamescasestudy.rest.Controllers;

import com.dreamgamescasestudy.rest.Models.User;
import com.dreamgamescasestudy.rest.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class UserController {

    @Autowired
    private UserRepo userRepository;

    @GetMapping(value = "/")
    public String getPage(){
        return "Welcome";
    }

    @GetMapping(value = "/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping(value = "/user/create")
    public User CreateUserRequest() {
        User newUser = new User();
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
