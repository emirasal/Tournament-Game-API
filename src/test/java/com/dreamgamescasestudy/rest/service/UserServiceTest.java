package com.dreamgamescasestudy.rest.service;
import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        String username = "testUser";
        User expectedUser = User.builder().username(username).build();

        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // When
        User createdUser = userService.createUser(username);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expectedUser, createdUser);
        assertEquals(username, createdUser.getUsername());
    }

    @Test
    void testUpdateUserLevel() {
        User existingUser = User.builder().userID(1L).level(1).build();
        Optional<User> optionalUser = Optional.of(existingUser);

        when(userRepository.findById(1L)).thenReturn(optionalUser);

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        User updatedUser = userService.updateUserLevel(1L);

        // Then
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);

        assertNotNull(updatedUser);
        assertEquals(existingUser.getLevel() , updatedUser.getLevel());
    }

    @Test
    void testClaimTournamentReward() {
        long userId = 1L;
        User existingUser = User.builder().userID(userId).coins(100).pendingCoins(50).build();
        Optional<User> optionalUser = Optional.of(existingUser);

        when(userRepository.findById(userId)).thenReturn(optionalUser);
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        User claimedUser = userService.claimTournamentReward(userId);

        // Then
        assertEquals(150, claimedUser.getCoins());
        assertEquals(0, claimedUser.getPendingCoins());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }
}