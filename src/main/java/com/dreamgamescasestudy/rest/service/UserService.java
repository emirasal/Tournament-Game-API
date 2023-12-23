package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.repository.UserRepository;
import com.dreamgamescasestudy.rest.domain.Country;
import com.dreamgamescasestudy.rest.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String username){
        User user = User.builder().country(Country.getRandomCountry()).username(username).build();
        return userRepository.save(user);
    }


}
