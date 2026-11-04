// Program: Chat log filter using split(), StringBuilder and keyword search
// main method acts as the Driver

import java.util.Scanner;

public class ChatFilter
{
    // Splits a log line into time, user and message (returns null if malformed)
    public static String[] splitLine(String line)
    {
        String[] parts = line.split(" ", 3);

        // A valid line must have exactly 3 parts
        if (parts.length < 3)
        {
            return null;
        }

        return parts;
    }

    // Checks whether message contains the keyword (case-insensitive)
    public static boolean hasKeyword(String message, String keyword)
    {
        return message.toLowerCase().contains(keyword.toLowerCase());
    }

    public static void main(String[] args)
    {
        // Log lines (the 4th line is malformed, only 2 parts)
        String[] log =
        {
            "3:28 Krisha Hello Honey",
            "4:34 Hinal Good Morning",
            "4:01 Honey How are you",
            "3:34 Panvi",
            "3:32 Krisha Good Morning"
        };

        Scanner sc = new Scanner(System.in);

        // Reading keyword from the user
        System.out.print("Enter the keyword: ");
        String keyword = sc.nextLine().trim();

        int match = 0;
        StringBuilder report = new StringBuilder();

        for (String line : log)
        {
            String[] parts = splitLine(line);

            // Skip malformed lines
            if (parts == null)
            {
                continue;
            }

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            // If message contains keyword, count it and add to report
            if (hasKeyword(message, keyword))
            {
                match++;
                report.append(time)
                      .append(" ")
                      .append(user)
                      .append(": ")
                      .append(message)
                      .append("\n");
            }
        }

        // Printing result
        System.out.println("Matches: " + match);
        System.out.print(report);

        sc.close();
    }
}