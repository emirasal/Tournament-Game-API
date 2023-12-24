package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.GroupLeaderboard;
import com.dreamgamescasestudy.rest.domain.TournamentGroup;
import com.dreamgamescasestudy.rest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupLeaderboardRepository extends JpaRepository<GroupLeaderboard, Long> {


    GroupLeaderboard findByUserAndTournamentGroupIn(User user, List<TournamentGroup> groups);

    List<GroupLeaderboard> findByTournamentGroup(TournamentGroup tournamentGroup);

    GroupLeaderboard findByUser(User user);

}
