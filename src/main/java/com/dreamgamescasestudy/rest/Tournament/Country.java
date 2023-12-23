package com.dreamgamescasestudy.rest.Tournament;

public class Country implements Comparable<Country> {

    private final String countryName;
    private int tournamentScore;

    public Country(String country) {
        this.countryName = country;
        this.tournamentScore = 0;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    @Override
    public int compareTo(Country other) {
        // Comparing countries based on score only
        return Integer.compare(this.tournamentScore, other.tournamentScore);
    }
}
