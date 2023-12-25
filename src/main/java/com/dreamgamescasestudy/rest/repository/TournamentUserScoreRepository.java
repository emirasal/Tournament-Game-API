package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.TournamentGroup;
import com.dreamgamescasestudy.rest.domain.TournamentUserScore;
import com.dreamgamescasestudy.rest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentUserScoreRepository extends JpaRepository<TournamentUserScore, Long> {

    TournamentUserScore findByUserAndTournamentGroupIn(User user, List<TournamentGroup> groups);
    List<TournamentUserScore> findByTournamentGroup(TournamentGroup tournamentGroup);

}
