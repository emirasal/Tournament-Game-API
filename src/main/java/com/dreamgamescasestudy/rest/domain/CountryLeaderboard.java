package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;

@Entity
public class CountryLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournamentID")
    private Tournament tournament;

    @Column
    private String country;

    @Column
    private int tournamentScore;


}
