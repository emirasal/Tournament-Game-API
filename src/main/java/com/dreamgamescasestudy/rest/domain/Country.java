package com.dreamgamescasestudy.rest.domain;

import java.util.Random;

public enum Country {
    TURKEY,
    UNITED_STATES,
    UNITED_KINGDOM,
    FRANCE,
    GERMANY;

    public static Country getRandomCountry() {
        Country[] values = Country.values();
        int length = values.length;
        Random random = new Random();
        int randomIndex = random.nextInt(length);
        return values[randomIndex];
    }
}