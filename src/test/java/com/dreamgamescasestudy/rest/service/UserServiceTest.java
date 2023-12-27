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
        // Given
        String username = "testUser";
        User expectedUser = User.builder().username(username).build();

        // Mocking userRepository.save() behavior
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        // When
        User createdUser = userService.createUser(username);

        // Then
        verify(userRepository, times(1)).save(any(User.class)); // Verifying that userRepository.save() was called
        assertEquals(expectedUser, createdUser); // Verifying that the createdUser matches the expectedUser
        assertEquals(username, createdUser.getUsername()); // Verifying specific attributes of the created user
    }

    @Test
    void testUpdateUserLevel() {
        // Given
        User existingUser = User.builder().userID(1L).level(1).build();
        Optional<User> optionalUser = Optional.of(existingUser);

        // Mocking userRepository.findById() behavior
        when(userRepository.findById(1L)).thenReturn(optionalUser);

        // Mocking userRepository.save() behavior
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            return invocation.<User>getArgument(0);
        });

        // When
        User updatedUser = userService.updateUserLevel(1L);

        // Then
        verify(userRepository, times(1)).findById(1L); // Verifying that userRepository.findById() was called
        verify(userRepository, times(1)).save(existingUser); // Verifying that userRepository.save() was called

        assertNotNull(updatedUser); // Verifying that the updatedUser is not null
        assertEquals(existingUser.getLevel() , updatedUser.getLevel()); // Verifying that the level is incremented

    }
}


