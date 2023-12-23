package com.dreamgamescasestudy.rest.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String username;

    @Column
    @Builder.Default
    private int level = 1;

    @Column
    @Builder.Default
    private int coin = 5000;

    @Column
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column
    private int pendingCoins;

    public User(String username) {
        this.username = username;
    }

    public void updateFieldsForNewLevel(){
        this.coin = getCoin() + 25;
        this.level = getLevel() + 1;
    }
}

