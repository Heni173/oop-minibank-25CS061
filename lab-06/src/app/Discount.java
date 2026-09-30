package app;

import discount.Rule;
import engine.DiscountEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Discount {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<Double> prices = new ArrayList<>();

        prices.add(1000.0);
        prices.add(2000.0);
        prices.add(5000.0);
        prices.add(10000.0);

        System.out.println("Select Discount Rule:");
        System.out.println("1. 10% Discount");
        System.out.println("2. 20% Discount");
        System.out.println("3. Rs. 500 Discount");

        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        Rule rule;

        switch (choice) {

            case 1:
                rule = price -> price - (price * 0.10);
                break;

            case 2:
                rule = price -> price - (price * 0.20);
                break;

            case 3:
                rule = price -> price - 500;
                break;

            default:
                System.out.println("Invalid choice");
                sc.close();
                return;
        }

        DiscountEngine engine = new DiscountEngine();

        System.out.println("\nDiscounted Prices:");

        engine.apply(prices, rule);

        sc.close();
    }
}