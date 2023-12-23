package com.dreamgamescasestudy.rest.web.response;

import com.dreamgamescasestudy.rest.domain.Country;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserProgressResponse {
    private Long id;
    private int level;
    private int coins;
    private Country country;

    public UserProgressResponse(Long id, int level, int coins, Country country) {
        this.id = id;
        this.level = level;
        this.coins = coins;
        this.country = country;
    }
}
