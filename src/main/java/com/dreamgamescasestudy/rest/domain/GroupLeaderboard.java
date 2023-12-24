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
    private Long leaderboardId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "group_id")
    private TournamentGroup tournamentGroup;


    @Column
    @Builder.Default
    private int userScore = 0;

}
