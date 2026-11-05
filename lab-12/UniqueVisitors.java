// Program: Unique visitors using HashSet and LinkedHashSet

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {

    public static void main(String[] args) {

        // List of login names (some names are repeated)
        List<String> logins = new ArrayList<>();
        logins.add("Heni");
        logins.add("Krisha");
        logins.add("Heni");
        logins.add("Keya");
        logins.add("Krisha");
        logins.add("Honey");
        logins.add("Heni");

        // HashSet removes the repeated names, so its size is the unique count
        Set<String> uniqueUsers = new HashSet<>(logins);

        // LinkedHashSet also removes repeats and keeps the first-seen order
        Set<String> orderedUsers = new LinkedHashSet<>(logins);

        System.out.println("Total logins : " + logins.size());
        System.out.println("Unique users : " + uniqueUsers.size());

        // Printing all distinct names in first-seen order
        System.out.println();
        System.out.println("All distinct names (first-seen order): " + orderedUsers);

        // Printing only the first three distinct names
        System.out.println();
        System.out.println("First three distinct names:");

        int count = 0;

        for (String name : orderedUsers) {
            System.out.println(name);
            count++;

            // Stopping after three names
            if (count == 3) {
                break;
            }
        }
    }
}