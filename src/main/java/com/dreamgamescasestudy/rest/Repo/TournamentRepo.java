package com.dreamgamescasestudy.rest.Repo;

import com.dreamgamescasestudy.rest.Models.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TournamentRepo extends JpaRepository<Tournament, Long> {
}
