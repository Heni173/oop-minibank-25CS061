// Program: Checking equivalent fractions
// FractionDemo is the Driver class of this program

public class FractionDemo
{
    public static void main(String[] args)
    {
        // Equivalent fractions
        Fraction f1 = new Fraction(1, 2);
        Fraction f2 = new Fraction(2, 4);
        Fraction f3 = new Fraction(3, 6);

        // Printing using toString()
        System.out.println("Fraction 1: " + f1);
        System.out.println("Fraction 2: " + f2);
        System.out.println("Fraction 3: " + f3);

        // Checking equality
        System.out.println("f1 equals f2: " + f1.equals(f2));
        System.out.println("f1 equals f3: " + f1.equals(f3));
        System.out.println("f2 equals f3: " + f2.equals(f3));

        // hashCode should also be same for equal fractions
        System.out.println("Same hashCode: " + (f1.hashCode() == f2.hashCode()
                && f2.hashCode() == f3.hashCode()));
    }
}