package com.dreamgamescasestudy.rest.Tournament;

import java.util.TreeSet;

public class SortedCountrySet {

    private TreeSet<Country> sortedCountries;
    private static final String[] countries = {"Turkey", "United States", "United Kingdom", "France", "Germany"};

    public SortedCountrySet() {
        sortedCountries = new TreeSet<>(Country::compareTo);
        // Initializing 5 countries as 0 score at the beginning
        for (String countryName : countries) {
            sortedCountries.add(new Country(countryName));
        }
    }

    public void increaseScore(String country) {
        // First remove the country with that name and add it after increasing the score by 1
        sortedCountries.
    }

    public TreeSet<Country> getSortedCountries() {
        return sortedCountries;
    }
}
