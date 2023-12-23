package com.dreamgamescasestudy.rest.Tournament;

import java.util.TreeSet;

public class SortedCountrySet {

    private final TreeSet<Country> sortedCountries;
    private static final String[] countries = {"Turkey", "United States", "United Kingdom", "France", "Germany"};

    public SortedCountrySet() {
        sortedCountries = new TreeSet<>(Country::compareTo);
        // Initializing 5 countries as 0 score at the beginning
        for (String countryName : countries) {
            sortedCountries.add(new Country(countryName));
        }
    }

    public void increaseScore(String countryName) {
        // First remove the country with that name and add it after increasing the score by 1
        for (Country country : sortedCountries) {
            if (country.getCountryName().equals(countryName)) {
                sortedCountries.remove(country); // Removing the country
                country.setTournamentScore(country.getTournamentScore() + 1); // Increasing the score
                sortedCountries.add(country); // Inserting the country with updated score
                break;
            }
        }
    }

    public TreeSet<Country> getSortedCountries() {
        return sortedCountries;
    }
}
