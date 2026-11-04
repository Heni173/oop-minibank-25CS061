// Program: Template filler using Pattern and Matcher

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Helper class that fills the template
class Filler {

    // Replaces each {placeholder} with its value, or [?] if not found
    public static String fill(String text, String[] names, String[] values) {

        // Pattern to find {word} placeholders
        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(text);

        StringBuilder result = new StringBuilder();

        int last = 0;   // end position of the previous match

        while (m.find()) {

            // Copy the text before this placeholder
            result.append(text.substring(last, m.start()));

            String name = m.group(1);
            String value = "[?]";   // default when value is not supplied

            // Look up the placeholder name in the names array
            for (int i = 0; i < names.length; i++) {

                if (names[i].equals(name)) {
                    value = values[i];
                    break;
                }
            }

            result.append(value);
            last = m.end();
        }

        // Copy the remaining text after the last placeholder
        result.append(text.substring(last));

        return result.toString();
    }
}

public class TemplateFiller {

    public static void main(String[] args) {

        // Template with three placeholders ({date} is not supplied)
        String text = "Dear {name}, order {id} ships {date}.";

        // Parallel arrays: names and their values
        String[] names = {"name", "id"};
        String[] values = {"Heni", "A07"};

        String ans = Filler.fill(text, names, values);
        System.out.println(ans);
    }
}