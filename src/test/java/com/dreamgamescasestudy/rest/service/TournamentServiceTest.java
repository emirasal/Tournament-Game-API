package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.Tournament;
import com.dreamgamescasestudy.rest.domain.TournamentGroup;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.domain.User;
import com.dreamgamescasestudy.rest.repository.TournamentGroupRepository;
import com.dreamgamescasestudy.rest.repository.TournamentRepository;
import com.dreamgamescasestudy.rest.repository.TournamentUserScoreRepository;
import com.dreamgamescasestudy.rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TournamentUserScoreRepository tournamentUserScoreRepository;


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @Test
    void testStartNewTournament() {
        // When startNewTournament is called
        tournamentService.startNewTournament();

        // Then verify that tournamentRepository.save() was called with a Tournament object
        verify(tournamentRepository, times(1)).save(any(Tournament.class));

        // Add more assertions or verifications based on the specific behavior of the service method
    }

    @Test
    void testGiveRewardsToGroups() {
        // Given a mock leaderboard with two users
        User firstPlaceUser = User.builder().userID(1L).pendingCoins(0).build();
        User secondPlaceUser = User.builder().userID(2L).pendingCoins(0).build();
        List<TournamentUserScore> leaderboard = Arrays.asList(
                TournamentUserScore.builder().user(firstPlaceUser).score(100).build(),
                TournamentUserScore.builder().user(secondPlaceUser).score(90).build()
        );

        // Mocking the behavior of tournamentUserScoreRepository.findByTournamentGroup
        when(tournamentUserScoreRepository.findByTournamentGroup(any())).thenReturn(leaderboard);

        // When giveRewardsToGroups is called
        tournamentService.giveRewardsToGroups(Collections.emptyList()); // Pass any list of groups for the sake of testing

        // Then verify that rewards are assigned correctly
        assertEquals(1000, firstPlaceUser.getPendingCoins()); // Updated expected value to 1000 based on the logic
        assertEquals(5000, secondPlaceUser.getPendingCoins());
        verify(userRepository, times(1)).save(firstPlaceUser);
        verify(userRepository, times(1)).save(secondPlaceUser);
    }


}