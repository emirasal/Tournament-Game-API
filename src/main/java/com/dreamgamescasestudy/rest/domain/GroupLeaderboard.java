package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
    private int userScore = 0;

}
