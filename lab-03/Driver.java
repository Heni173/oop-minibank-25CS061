public class Driver
{
    public static void main(String[] args)
    {
        Card[] cards = new Card[5];

        int count = 0;

        Card c1 = new Card("Ace", "Spades");

        cards[count] = c1;
        count++;

        Card c2 = new Card("King", "Hearts");

        for(int i = 0; i < count; i++)
        {
            if(c2.equals(cards[i]))
            {
                System.out.println("Duplicate found: " + c2);
                return;
            }
        }

        cards[count] = c2;
        count++;

        Card c3 = new Card("Queen", "Diamonds");

        for(int i = 0; i < count; i++)
        {
            if(c3.equals(cards[i]))
            {
                System.out.println("Duplicate found: " + c3);
                return;
            }
        }

        cards[count] = c3;
        count++;

        Card c4 = new Card("Ace", "Spades");

        for(int i = 0; i < count; i++)
        {
            if(c4.equals(cards[i]))
            {
                System.out.println("Duplicate found: " + c4);
                return;
            }
        }

        cards[count] = c4;
        count++;
    }
}