// Program: Guarded calculator using custom exception, try-catch-finally and loop

import java.util.*;

// Custom exception for dividing by zero
class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {

    public static void main(String arg[]) {

        Scanner sc = new Scanner(System.in);
        boolean success = false;   // becomes true when a calculation works
        int attempt = 0;           // counts the attempts

        // Loop until a valid calculation succeeds
        while (!success) {

            attempt++;
            String status = "Failed";   // result of this attempt, used in the log

            try {

                // Reading two numbers and an operator
                System.out.println("Enter First Number:");
                double num1 = Double.parseDouble(sc.nextLine());

                System.out.println("Enter Operator:");
                String op = sc.nextLine().trim();

                System.out.println("Enter Second Number:");
                double num2 = Double.parseDouble(sc.nextLine());

                // Throwing the custom exception for division by zero
                if (num2 == 0 && op.equals("/")) {
                    throw new DivideByZeroException("Cannot divide by zero!");
                }

                double result;

                switch (op) {

                    case "+":
                        result = num1 + num2;
                        break;

                    case "-":
                        result = num1 - num2;
                        break;

                    case "*":
                        result = num1 * num2;
                        break;

                    case "/":
                        result = num1 / num2;
                        break;

                    default:
                        // Wrong operator: try again
                        System.out.println("Invalid operator! Use + - * or /");
                        continue;
                }

                System.out.println(num1 + " " + op + " " + num2 + " = " + result);

                success = true;
                status = "Success";
            }

            // Custom exception: print its clear message
            catch (DivideByZeroException d) {
                System.out.println(d.getMessage());
            }

            // Invalid number input is handled separately
            catch (NumberFormatException n) {
                System.out.println("Invalid number! Please enter digits only.");
            }

            // finally always runs, so every attempt is logged
            finally {
                System.out.println("[LOG] Attempt " + attempt + " - " + status);
            }
        }

        sc.close();
    }
}