package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.Tournament;
import com.dreamgamescasestudy.rest.domain.TournamentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TournamentGroupRepository extends JpaRepository<TournamentGroup, Long> {
    List<TournamentGroup> findByTournament(Tournament tournament);

}
