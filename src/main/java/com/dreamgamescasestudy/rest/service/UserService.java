package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.repository.UserRepository;
import com.dreamgamescasestudy.rest.domain.Country;
import com.dreamgamescasestudy.rest.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String username){
        User user = User.builder().country(Country.getRandomCountry()).username(username).build();
        return userRepository.save(user);
    }

    public User updateUserLevel(Long userID){
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.updateFieldsForNewLevel();
            return userRepository.save(user);
        }
        // User does not exist!
        return null;
    }

    public User ClaimTournamentReward(Long userID) {
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.setCoins(user.getCoins() + user.getPendingCoins());
            user.setPendingCoins(0);

            userRepository.save(user);
        }
        // User does not exist!
        return null;
    }


}
