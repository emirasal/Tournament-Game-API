package com.dreamgamescasestudy.rest.Tournament;

import com.dreamgamescasestudy.rest.Model.User;

public class Player implements Comparable<Player> {

    private final long userID;
    private final String username;
    private final String country;
    private int tournamentScore;

    public Player(User user) {
        this.userID = user.getId();
        this.username = user.getUsername();
        this.country = user.getCountry();
        this.tournamentScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public int gettournamentScore() {
        return tournamentScore;
    }

    @Override
    public int compareTo(Player other) {
        // Comparing players based on score only
        return Integer.compare(this.tournamentScore, other.tournamentScore);
    }
}
