package com.dreamgamescasestudy.rest.Tournament;

public class Country implements Comparable<Country> {

    private final String country;
    private int tournamentScore;

    public Country(String country) {
        this.country = country;
        this.tournamentScore = 0;
    }

    @Override
    public int compareTo(Country other) {
        // Comparing countries based on score only
        return Integer.compare(this.tournamentScore, other.tournamentScore);
    }
}
