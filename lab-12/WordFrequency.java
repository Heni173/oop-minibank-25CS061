// Program: Word frequency using HashMap and Comparator

import java.util.*;

// Comparator to sort entries by frequency (highest first)
class FrequencyComparator implements Comparator<Map.Entry<String, Integer>>
{
    public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b)
    {
        int countA = a.getValue();
        int countB = b.getValue();

        if (countA > countB)
        {
            return -1;      // a comes first because it has a higher count
        }
        else if (countA < countB)
        {
            return 1;       // b comes first because it has a higher count
        }
        else
        {
            // Same count: sort alphabetically, so the output is always the same
            return a.getKey().compareTo(b.getKey());
        }
    }
}

public class WordFrequency
{
    public static void main(String a[])
    {
        String paragraph = "Java is fun and Java is powerful and I love Java";

        // Making everything lowercase, so "Java" and "java" are the same word
        paragraph = paragraph.toLowerCase();

        // Splitting the paragraph into words (anything that is not a letter or digit is a separator)
        String[] words = paragraph.split("\\W+");

        // Map to store each word and how many times it came
        HashMap<String, Integer> map = new HashMap<>();

        for (int i = 0; i < words.length; i++)
        {
            String word = words[i];

            // Skipping empty words
            if (word.equals(""))
            {
                continue;
            }

            // If the word is already in the map, increase its count, otherwise add it with 1
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

        // Copying the entries of the map into a list, because a HashMap cannot be sorted
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(map.entrySet());

        // Sorting the list using our comparator
        Collections.sort(list, new FrequencyComparator());

        // Printing the words, highest frequency first
        System.out.println("Word Frequency:");

        for (int i = 0; i < list.size(); i++)
        {
            Map.Entry<String, Integer> entry = list.get(i);
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}