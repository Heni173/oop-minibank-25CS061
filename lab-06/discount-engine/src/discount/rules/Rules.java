package discount.rules;

import java.util.LinkedHashMap;
import java.util.Map;

// Holds all available discount rules, each written as a lambda
public class Rules
{
    // LinkedHashMap keeps the rules in the same order as they are added
    private static final Map<String, DiscountRule> RULES = new LinkedHashMap<>();

    static
    {
        // Rule 1: 10% off on every price
        RULES.put("10% Off", price -> price * 0.90);

        // Rule 2: flat Rs. 50 off, but price never goes below 0
        RULES.put("Flat Rs.50 Off", price -> Math.max(price - 50, 0));

        // Rule 3: 20% off for prices above 1000, otherwise only 5% off
        RULES.put("Big Spender (20% above 1000, else 5%)",
                price -> price > 1000 ? price * 0.80 : price * 0.95);
    }

    // Returns all rule names (used to show the menu)
    public static String[] names()
    {
        return RULES.keySet().toArray(new String[0]);
    }

    // Returns the rule for the given name
    public static DiscountRule get(String name)
    {
        return RULES.get(name);
    }
}