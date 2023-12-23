package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupID;

    @ManyToOne
    @JoinColumn(name = "tournamentID")
    private Tournament tournament; // Reference to Tournament

    @Column
    @Builder.Default
    boolean playing = false;

}
