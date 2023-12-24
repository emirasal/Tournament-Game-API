package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CountryLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaderboardId;

    @ManyToOne
    @JoinColumn(name = "tournament")
    private Tournament tournament;

    @Column
    private Country country;

    @Column
    private int score;


}
