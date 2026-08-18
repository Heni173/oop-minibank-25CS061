import java.util.Scanner;

public class ChatFilter {

    public static void main(String arg[]) {

        String log[] = {
            "3:28 Krisha Hello Honey",
            "4:34 Hinal Good Morning",
            "4:01 Honey How are you",
            "3:34 Panvi",
            "3:32 Krisha Good Morning"
        };

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the keyword:");
        String keyword = sc.next();

        int match = 0;
        StringBuilder report = new StringBuilder();

        for (String line : log) {

            String[] parts = ChatFilter.splitLine(line);

            if (parts == null)
                continue;

            String time = parts[0];
            String name = parts[1];
            String message = parts[2];

            if (ChatFilter.hasKeyword(message, keyword)) {

                match++;

                report.append(time)
                      .append(" ")
                      .append(name)
                      .append(" ")
                      .append(message)
                      .append("/");
            }
        }

        System.out.println("Matches: " + match);
        System.out.println("Report: " + report);

        sc.close();
    }

    public static String[] splitLine(String line) {

        String[] parts = line.split(" ", 3);

        if (parts.length <= 2)
            return null;

        return parts;
    }

    public static boolean hasKeyword(String message, String keyword) {

        return message.toLowerCase().contains(keyword.toLowerCase());
    }
}