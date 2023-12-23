package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;

@Entity
public class GroupLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", referencedColumnName = "id")
    private TournamentGroup tournamentGroup;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column
    private int userScore;

}
