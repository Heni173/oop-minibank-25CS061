// Program: Card class with toString, equals and hashCode

import java.util.Objects;

public class Card
{
    // Rank and suit of the card
    private String rank;
    private String suit;

    // Constructor to set rank and suit
    public Card(String rank, String suit)
    {
        this.rank = rank;
        this.suit = suit;
    }

    // Returns card in "Queen of Hearts" format
    @Override
    public String toString()
    {
        return rank + " of " + suit;
    }

    // Two cards are equal when rank and suit both match
    @Override
    public boolean equals(Object ob)
    {
        if (this == ob)
        {
            return true;
        }

        if (!(ob instanceof Card))
        {
            return false;
        }

        Card c = (Card) ob;
        return rank.equals(c.rank) && suit.equals(c.suit);
    }

    // hashCode must agree with equals()
    @Override
    public int hashCode()
    {
        return Objects.hash(rank, suit);
    }
}