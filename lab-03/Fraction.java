public class Fraction
{
    private int num;
    private int den;

    public Fraction(int num, int den)
    {
        int g = gcd(num, den);

        this.num = num / g;
        this.den = den / g;
    }

    public int gcd(int a, int b)
    {
        while(b != 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    @Override
    public String toString()
    {
        return num + "/" + den;
    }

    @Override
    public boolean equals(Object ob)
    {
        if(this == ob)
            return true;

        if(!(ob instanceof Fraction))
            return false;

        Fraction f = (Fraction) ob;

        return num == f.num && den == f.den;
    }
}