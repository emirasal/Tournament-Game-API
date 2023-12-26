package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
