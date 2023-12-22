package com.dreamgamescasestudy.rest.Model;

import jakarta.persistence.*;

import java.util.Random;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private final String username;
    @Column
    private int level;
    @Column
    private int coins;
    @Column
    private final String country;
    @Column
    private int pendingCoins;
    @Transient // Not mapped to the database
    private static final String[] countries = {"Turkey", "United States", "United Kingdom", "France", "Germany"};

    public User(String username) {
        this.username = username;
        this.level = 1;
        this.coins = 5000;
        this.pendingCoins = 0;

        // Random Country
        Random random = new Random();
        int index = random.nextInt(countries.length);
        this.country = countries[index];
    }

    public String getUsername() {
        return username;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setPendingCoins(int pendingCoins) {
        this.pendingCoins = pendingCoins;
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

    public int getPendingCoins() {
        return pendingCoins;
    }
}
