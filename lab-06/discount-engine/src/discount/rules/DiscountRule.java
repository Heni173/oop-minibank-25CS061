package discount.rules;

// Functional interface: one discount rule takes a price and returns the discounted price
@FunctionalInterface
public interface DiscountRule
{
    double apply(double price);
}