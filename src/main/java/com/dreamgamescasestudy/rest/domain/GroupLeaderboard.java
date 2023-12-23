package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class GroupLeaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaderboard_id;

    @ManyToOne
    @JoinColumn(name = "groupID")
    private TournamentGroup tournamentGroup;


    @ManyToOne
    @JoinColumn(name = "userID")
    private User user;


    @Column
    private String username;

    @Column
    private int userScore;

}
