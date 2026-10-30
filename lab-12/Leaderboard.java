import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    public static void main(String[] args) {
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

        int topN = 5;

        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());

        list.sort((a, b) -> {
            if (a.getValue() != b.getValue()) {
                return b.getValue() - a.getValue();
            }
            return a.getKey().compareTo(b.getKey());
        });

        int limit = Math.min(topN, list.size());

        System.out.println("TOP " + limit + " PLAYERS");
        System.out.println("-----------------------");
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> player = list.get(i);
            System.out.println((i + 1) + ". " + player.getKey() + " - " + player.getValue());
        }
    }
}
