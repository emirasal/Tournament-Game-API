package com.dreamgamescasestudy.rest.web.response;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class GroupLeaderboardResponse {

    private final String username;
    private final int userScore;

    public GroupLeaderboardResponse(String username, int userScore) {
        this.username = username;
        this.userScore = userScore;
    }
}
