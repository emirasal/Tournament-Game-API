package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.Country;
import com.dreamgamescasestudy.rest.domain.CountryLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryLeaderboardRepository extends JpaRepository<CountryLeaderboard, Long> {

    List<CountryLeaderboard> findByTournamentTournamentId(Long tournamentId);

}
