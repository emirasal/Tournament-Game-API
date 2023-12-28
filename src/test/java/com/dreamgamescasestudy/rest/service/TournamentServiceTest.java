package com.dreamgamescasestudy.rest.service;

import com.dreamgamescasestudy.rest.domain.*;
import com.dreamgamescasestudy.rest.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TournamentServiceTest {

    @Mock
    private TournamentRepository tournamentRepository;
    @Mock
    private TournamentUserScoreRepository tournamentUserScoreRepository;
    @Mock
    private TournamentGroupRepository tournamentGroupRepository;
    @Mock
    private TournamentCountryScoreRepository tournamentCountryScoreRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TournamentService tournamentService;

    @Test
    void testStartNewTournament() {
        tournamentService.startNewTournament();

        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    void testGiveRewardsToGroups() {
        TournamentUserScore firstPlace = TournamentUserScore.builder().user(User.builder().userID(1L).pendingCoins(0).build()).score(100).build();
        TournamentUserScore secondPlace = TournamentUserScore.builder().user(User.builder().userID(2L).pendingCoins(0).build()).score(90).build();
        List<TournamentUserScore> leaderboard = Arrays.asList(firstPlace, secondPlace);

        TournamentGroup group = TournamentGroup.builder().groupID(1L).build();
        when(tournamentUserScoreRepository.findByTournamentGroup(group)).thenReturn(leaderboard);

        // When
        tournamentService.giveRewardsToGroups(Collections.singletonList(group));

        // Then
        verify(tournamentUserScoreRepository).findByTournamentGroup(group);

        assertEquals(10000, firstPlace.getUser().getPendingCoins());
        assertEquals(5000, secondPlace.getUser().getPendingCoins());
        verify(userRepository, times(1)).save(firstPlace.getUser());
        verify(userRepository, times(1)).save(secondPlace.getUser());
    }

    @Test
    void testGetGroupRank() {
        Tournament tournament = Tournament.builder().tournamentID(1L).build();
        User user1 = User.builder().userID(1L).build();
        User user2 = User.builder().userID(2L).build();
        TournamentGroup group = TournamentGroup.builder().groupID(1L).build();
        TournamentUserScore userScore1 = TournamentUserScore.builder().user(user1).tournamentGroup(group).score(100).build();
        TournamentUserScore userScore2 = TournamentUserScore.builder().user(user2).tournamentGroup(group).score(90).build();

        Optional<Tournament> optionalTournament = Optional.of(tournament);
        Optional<User> optionalUser = Optional.of(user1);

        when(tournamentRepository.findById(1L)).thenReturn(optionalTournament);
        when(userRepository.findById(1L)).thenReturn(optionalUser);

        when(tournamentGroupRepository.findByTournament(tournament)).thenReturn(Collections.singletonList(group));

        when(tournamentUserScoreRepository.findByUserAndTournamentGroupIn(user1, Collections.singletonList(group)))
                .thenReturn(userScore1);

        when(tournamentUserScoreRepository.findByTournamentGroup(group))
                .thenReturn(Arrays.asList(userScore1, userScore2)); // Adding user2

        // When
        int rank = tournamentService.getGroupRank(1L, 1L);

        // Then
        assertEquals(1, rank);
    }

    @Test
    void testGetGroupLeaderboard() {
        TournamentGroup group = TournamentGroup.builder().groupID(1L).build();
        TournamentUserScore userScore1 = TournamentUserScore.builder().score(100).build();
        TournamentUserScore userScore2 = TournamentUserScore.builder().score(90).build();

        when(tournamentGroupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(tournamentUserScoreRepository.findByTournamentGroup(group)).thenReturn(Arrays.asList(userScore1, userScore2));

        // When
        List<TournamentUserScore> leaderboard = tournamentService.getGroupLeaderboard(1L);

        // Then
        assertEquals(2, leaderboard.size());
        assertEquals(100, leaderboard.get(0).getScore());
        assertEquals(90, leaderboard.get(1).getScore());

        verify(tournamentGroupRepository, times(1)).findById(anyLong());
        verify(tournamentUserScoreRepository, times(1)).findByTournamentGroup(group);
    }

    @Test
    void testGetCountryLeaderboard() {
        Tournament tournament = Tournament.builder().tournamentID(1L).build();
        TournamentCountryScore score1 = TournamentCountryScore.builder().score(100).build();
        TournamentCountryScore score2 = TournamentCountryScore.builder().score(90).build();

        when(tournamentRepository.findById(1L)).thenReturn(Optional.of(tournament));
        when(tournamentCountryScoreRepository.findByTournament(tournament)).thenReturn(Arrays.asList(score1, score2));

        // When
        List<TournamentCountryScore> leaderboard = tournamentService.getCountryLeaderboard(1L);

        // Then
        assertEquals(2, leaderboard.size());
        assertEquals(100, leaderboard.get(0).getScore());
        assertEquals(90, leaderboard.get(1).getScore());

        verify(tournamentRepository, times(1)).findById(anyLong());
        verify(tournamentCountryScoreRepository, times(1)).findByTournament(tournament);
    }
}