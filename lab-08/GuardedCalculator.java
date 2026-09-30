import java.util.*;

class DivideByZeroException extends Exception {
    public DivideByZeroException(String message) {
        super(message);
    }
}

public class GuardedCalculator {

    public static void main(String arg[]) {

        Scanner sc = new Scanner(System.in);
        boolean success = false;
        int attempt = 0;

        while (!success) {

            attempt++;
            Double num1, num2;

            try {

                System.out.println("Enter First Number:");
                num1 = Double.parseDouble(sc.nextLine());

                System.out.println("Enter Operator:");
                String op = sc.nextLine().trim();

                System.out.println("Enter Second Number:");
                num2 = Double.parseDouble(sc.nextLine());

                if (num2 == 0 && op.equals("/")) {
                    throw new DivideByZeroException("Divided By Zero Exception");
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
                        System.out.println("Invalid operator!");
                        continue;
                }

                System.out.println(num1 + " " + op + " " + num2 + " = " + result);

                success = true;
            }

            catch (DivideByZeroException d) {
                System.out.println(d);
            }

            catch (NumberFormatException n) {
                System.out.println(n);
            }

            finally {
                System.out.println("[LOG] Attempt " + attempt);
            }
        }

        sc.close();
    }
}