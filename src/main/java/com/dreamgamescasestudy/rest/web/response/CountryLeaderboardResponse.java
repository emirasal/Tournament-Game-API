package com.dreamgamescasestudy.rest.web.response;

import com.dreamgamescasestudy.rest.domain.Country;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class CountryLeaderboardResponse {

    private final Country country;
    private final int score;
}
