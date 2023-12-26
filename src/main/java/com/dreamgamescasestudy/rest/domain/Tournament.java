package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentID;

    @Column
    @Builder.Default
    @Setter
    private TournamentState tournamentState = TournamentState.ACTIVE;

}
