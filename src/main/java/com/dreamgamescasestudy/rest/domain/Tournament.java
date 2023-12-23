package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column
    @Builder.Default
    @Getter
    @Setter
    private TournamentState tournamentState = TournamentState.ACTIVE;

}
