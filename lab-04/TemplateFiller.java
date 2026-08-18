import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Filler {

    public static String fill(String text, String[] names, String[] values) {

        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(text);

        StringBuilder result = new StringBuilder();

        int last = 0;

        while (m.find()) {

            result.append(text.substring(last, m.start()));

            String name = m.group(1);
            String value = "[?]";

            for (int i = 0; i < names.length; i++) {

                if (names[i].equals(name)) {
                    value = values[i];
                    break;
                }
            }

            result.append(value);
            last = m.end();
        }

        result.append(text.substring(last));

        return result.toString();
    }
}

public class TemplateFiller {

    public static void main(String[] args) {

        String text = "Dear {name}, order {id} ships {date}.";

        String[] names = {"name", "id"};
        String[] values = {"Heni", "H17"};

        String ans = Filler.fill(text, names, values);
        System.out.println(ans);
    }
}