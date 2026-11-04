package discount.app;

import discount.engine.DiscountEngine;
import discount.rules.DiscountRule;
import discount.rules.Rules;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Driver class: user picks a discount rule at runtime
public class DiscountApp
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        // Prices of the items in the cart
        List<Double> prices = Arrays.asList(500.0, 1200.0, 40.0, 2500.0);

        System.out.println("Original prices: " + prices);
        System.out.println();

        // Showing the menu of rules
        String[] names = Rules.names();
        System.out.println("Choose a discount rule:");
        for (int i = 0; i < names.length; i++)
        {
            System.out.println((i + 1) + ". " + names[i]);
        }

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        // Checking the choice
        if (choice < 1 || choice > names.length)
        {
            System.out.println("Invalid choice");
            sc.close();
            return;
        }

        // Getting the rule (lambda) chosen by the user
        String chosen = names[choice - 1];
        DiscountRule rule = Rules.get(chosen);

        // Applying the rule to all prices
        List<Double> discounted = DiscountEngine.applyAll(prices, rule);

        System.out.println();
        System.out.println("Rule applied: " + chosen);
        for (int i = 0; i < prices.size(); i++)
        {
            System.out.printf("Rs. %.2f -> Rs. %.2f%n", prices.get(i), discounted.get(i));
        }

        double before = DiscountEngine.total(prices);
        double after = DiscountEngine.total(discounted);

        System.out.println();
        System.out.printf("Total before: Rs. %.2f%n", before);
        System.out.printf("Total after : Rs. %.2f%n", after);
        System.out.printf("You saved   : Rs. %.2f%n", before - after);

        sc.close();
    }
}