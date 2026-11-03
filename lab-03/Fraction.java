// Program: Fraction class with reduction to lowest terms, equals and hashCode

import java.util.Objects;

public class Fraction
{
    // Numerator and denominator (always stored in lowest terms)
    private int num;
    private int den;

    // Constructor reduces the fraction to lowest terms
    public Fraction(int num, int den)
    {
        // Denominator cannot be zero
        if (den == 0)
        {
            throw new IllegalArgumentException("Denominator cannot be zero");
        }

        // Keep the sign in the numerator, denominator stays positive
        if (den < 0)
        {
            num = -num;
            den = -den;
        }

        int g = gcd(Math.abs(num), den);

        // If num is 0, gcd is den, so fraction becomes 0/1
        this.num = num / g;
        this.den = den / g;
    }

    // Finds gcd of two numbers using Euclid's algorithm
    private static int gcd(int a, int b)
    {
        while (b != 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    // Returns fraction in "num/den" format
    @Override
    public String toString()
    {
        return num + "/" + den;
    }

    // Two fractions are equal when reduced num and den match
    @Override
    public boolean equals(Object ob)
    {
        if (this == ob)
        {
            return true;
        }

        if (!(ob instanceof Fraction))
        {
            return false;
        }

        Fraction f = (Fraction) ob;
        return num == f.num && den == f.den;
    }

    // hashCode must agree with equals()
    @Override
    public int hashCode()
    {
        return Objects.hash(num, den);
    }
}