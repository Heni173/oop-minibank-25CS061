package engine;

import discount.Rule;
import java.util.List;

public class DiscountEngine {

    public void apply(List<Double> prices, Rule rule) {

        for (double price : prices) {
            double result = rule.apply(price);
            System.out.println(price + " -> " + result);
        }
    }
}
