import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {

    public static void main(String[] args) {
        List<String> logins = new ArrayList<>();
        logins.add("amit");
        logins.add("riya");
        logins.add("amit");
        logins.add("kiran");
        logins.add("riya");
        logins.add("neha");
        logins.add("amit");
        logins.add("kiran");
        logins.add("raj");
        logins.add("neha");

        Set<String> uniqueUsers = new HashSet<>(logins);
        Set<String> orderedUsers = new LinkedHashSet<>(logins);

        System.out.println("Total logins : " + logins.size());
        System.out.println("Unique users : " + uniqueUsers.size());

        System.out.println();
        System.out.println("First three distinct names:");
        int count = 0;
        for (String name : orderedUsers) {
            System.out.println(name);
            count++;
            if (count == 3) {
                break;
            }
        }
    }
}
