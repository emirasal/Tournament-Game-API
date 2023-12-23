package com.dreamgamescasestudy.rest.Tournament;


import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map;

public class SortedPlayerSet {

    private final TreeSet<Player> sortedPlayers;
    private final Map<Long, Player> playersMap; // Map to store the players ID


    public SortedPlayerSet() {
        // Initializing TreeSet with comparator based on score
        sortedPlayers = new TreeSet<>(Player::compareTo);
        playersMap = new TreeMap<>();
    }

    public void add(Player player) {
        sortedPlayers.add(player);
        playersMap.put(player.getUserID(), player);
    }

    public void increaseScore(long userID){
       // Removing the player with that ID and adding it again with the score increased by one
        Player player = playersMap.get(userID);
        if (player != null) {
            sortedPlayers.remove(player); // Removing the player from TreeMap
            player.setTournamentScore(player.getTournamentScore() + 1); // Increasing the score
            sortedPlayers.add(player); // Re-inserting the player with updated score
        }
    }

    public TreeSet<Player> getSortedPlayers() {
        return sortedPlayers;
    }

}
