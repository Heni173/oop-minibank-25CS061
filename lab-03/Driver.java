// Program: Detecting the first duplicate card using equals()

public class Driver
{
    public static void main(String[] args)
    {
        // Cards to be added one by one (Ace of Spades is repeated)
        Card[] incoming =
        {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),     // duplicate of first
            new Card("Ten", "Clubs")
        };

        Card[] cards = new Card[incoming.length];
        int count = 0;
        boolean found = false;

        for (int i = 0; i < incoming.length && !found; i++)
        {
            Card current = incoming[i];

            // Check current card against all earlier cards
            for (int j = 0; j < count; j++)
            {
                if (current.equals(cards[j]))
                {
                    System.out.println("Duplicate found: " + current);
                    found = true;     // stop after the first duplicate
                    break;
                }
            }

            // Add the card only if it is not a duplicate
            if (!found)
            {
                cards[count] = current;
                count++;
                System.out.println("Added: " + current);
            }
        }
    }
}