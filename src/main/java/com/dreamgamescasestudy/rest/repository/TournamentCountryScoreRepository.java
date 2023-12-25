package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.Country;
import com.dreamgamescasestudy.rest.domain.TournamentCountryScore;
import com.dreamgamescasestudy.rest.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentCountryScoreRepository extends JpaRepository<TournamentCountryScore, Long> {
    List<TournamentCountryScore> findByTournament(Tournament tournament);
    TournamentCountryScore findByTournamentAndCountry(Tournament tournament, Country country);

}
