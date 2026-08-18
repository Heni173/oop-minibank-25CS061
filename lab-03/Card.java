public class Card
{
    private String rank;
    private String suit;

    public Card(String rank, String suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }

    @Override
    public boolean equals(Object ob)
    {
        if(this == ob)
            return true;

        if(!(ob instanceof Card))
            return false;

        Card c = (Card) ob;

        return rank.equals(c.rank) && suit.equals(c.suit);
    }
}