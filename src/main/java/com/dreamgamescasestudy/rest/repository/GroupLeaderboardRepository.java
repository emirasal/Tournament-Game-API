package com.dreamgamescasestudy.rest.repository;

import com.dreamgamescasestudy.rest.domain.GroupLeaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupLeaderboardRepository extends JpaRepository<GroupLeaderboard, Long> {

    // Finding groupID, userID pair and return that groupID
    Long findGroupIdByUserIdAndGroupId(Long userId, Long groupId);

    List<GroupLeaderboard> findByGroupId(Long groupId);

}
