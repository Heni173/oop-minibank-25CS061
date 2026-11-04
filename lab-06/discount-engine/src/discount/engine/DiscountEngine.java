package discount.engine;

import discount.rules.DiscountRule;
import java.util.ArrayList;
import java.util.List;

// Applies a chosen rule to a list of prices
public class DiscountEngine
{
    // Returns a new list with the discounted prices
    public static List<Double> applyAll(List<Double> prices, DiscountRule rule)
    {
        List<Double> result = new ArrayList<>();

        for (double price : prices)
        {
            result.add(rule.apply(price));   // the lambda chosen by the user runs here
        }

        return result;
    }

    // Adds up all prices in the list
    public static double total(List<Double> prices)
    {
        double sum = 0;

        for (double price : prices)
        {
            sum += price;
        }

        return sum;
    }
}