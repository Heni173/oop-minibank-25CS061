// Program: Leaderboard from a Map of scores
// Sorted by score (highest first), ties are broken by name (A to Z)

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    public static void main(String[] args) {

        // Player names and their scores
        Map<String, Integer> scores = new HashMap<>();
        scores.put("amit", 85);
        scores.put("riya", 92);
        scores.put("kiran", 78);
        scores.put("neha", 92);
        scores.put("raj", 85);
        scores.put("simran", 60);
        scores.put("dev", 78);
        scores.put("meera", 99);
        scores.put("arjun", 85);
        scores.put("zoya", 70);

        // How many players to show
        int topN = 5;

        // A HashMap cannot be sorted, so copying its entries into a list
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());

        // Sorting the list: higher score first, same score then name A to Z
        list.sort((a, b) -> {

            int scoreA = a.getValue();
            int scoreB = b.getValue();

            // Different scores: the higher score comes first
            if (scoreA != scoreB) {
                return scoreB - scoreA;
            }

            // Same score: sorting by name
            return a.getKey().compareTo(b.getKey());
        });

        // If there are fewer players than topN, show all of them
        int limit = Math.min(topN, list.size());

        System.out.println("TOP " + limit + " PLAYERS");
        System.out.println("-----------------------");

        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> player = list.get(i);
            System.out.println((i + 1) + ". " + player.getKey() + " - " + player.getValue());
        }
    }
}