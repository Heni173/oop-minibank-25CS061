
import java.util.*;

class FrequencyComparator implements Comparator<Map.Entry<String, Integer>>
{
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b)
    {
        int countA = a.getValue();
        int countB = b.getValue();

        if (countA > countB)
        {
            return -1;
        }
        else if (countA < countB)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}

public class WordFrequency
{
    public static void main(String a[])
    {
        String paragraph = "My name is Heni and I am a student of Computer Science and Engineering.";

        paragraph = paragraph.toLowerCase();

        String[] words = paragraph.split("\\W+");

        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];

            if (word.equals(""))
            {
                continue;
            }

            if (map.containsKey(word))
            {
                int oldCount = map.get(word);
                map.put(word, oldCount + 1);
            }
            else
            {
                map.put(word, 1);
            }
        }

        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        Collections.sort(list, new FrequencyComparator());

        System.out.println("Word Frequency:");

        for (int i = 0; i < list.size(); i++)
        {
            Map.Entry<String, Integer> entry = list.get(i);
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
