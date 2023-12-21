package com.dreamgamescasestudy.rest.Models;

import jakarta.persistence.*;

import java.util.Random;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private int level;

    @Column
    private int coins;

    @Column
    private final String country;

    @Transient // Not mapped to the database
    private final String[] countries = {"Turkey", "United States", "United Kingdom", "France", "Germany"};

    public User() {
        this.level = 1;
        this.coins = 5000;

        // Random Country
        Random random = new Random();
        int index = random.nextInt(countries.length);
        this.country = countries[index];
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public long getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getCoins() {
        return coins;
    }

    public String getCountry() {
        return country;
    }
}
