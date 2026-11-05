import java.util.*;

class VendingMachine
{
    // Creating enum for coins
    enum Coin
    {
        ONE, TWO, FIVE, TEN
    }

    public static void main(String[] args)
    {
        // Setting price and total amount
        final int PRICE = 15;
        int total = 0;

        // Creating Scanner object
        Scanner sc = new Scanner(System.in);

        // Taking coins until total reaches 15
        while(total < PRICE)
        {
            System.out.println("Enter the coin:");
            String input = sc.nextLine().trim();

            Coin c;

            // Converting input into coin
            try
            {
                c = Coin.valueOf(input.toUpperCase());
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Invalid coin! Please try again.");
                continue;
            }

            // Getting coin value using switch expression
            int value = switch(c)
            {
                case ONE -> 1;
                case TWO -> 2;
                case FIVE -> 5;
                case TEN -> 10;
            };

            // Adding coin value to total
            total = total + value;

            System.out.println("Total paid: " + total);
        }

        // Printing remaining change
        System.out.println("Paid. Change: " + (total - PRICE));

        // Closing Scanner
        sc.close();
    }
}