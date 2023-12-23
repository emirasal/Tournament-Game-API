package com.dreamgamescasestudy.rest.Tournament;

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

    public long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    @Override
    public int compareTo(Player other) {
        // Comparing players based on score only
        return Integer.compare(this.tournamentScore, other.tournamentScore);
    }
}
