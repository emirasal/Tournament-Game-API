package com.dreamgamescasestudy.rest.Tournament;


import java.util.TreeSet;

public class SortedPlayerSet {

    private TreeSet<Player> sortedPlayers;

    public SortedPlayerSet() {
        // Initializing TreeSet with comparator based on score
        sortedPlayers = new TreeSet<>(Player::compareTo);
    }

    public void add(Player player) {
        sortedPlayers.add(player);
    }

    public TreeSet<Player> getSortedPlayers() {
        return sortedPlayers;
    }

}
