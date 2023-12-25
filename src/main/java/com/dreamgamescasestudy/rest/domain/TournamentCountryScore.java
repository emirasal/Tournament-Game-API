package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentCountryScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tournamentCountryId;

    @ManyToOne
    @JoinColumn(name = "tournament")
    private Tournament tournament;

    @Column
    private Country country;

    @Column
    @Builder.Default
    private int score = 0;

}
