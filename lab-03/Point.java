public class Point
{
    private int x;
    private int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object ob)
    {
        if(this == ob)
            return true;

        if(!(ob instanceof Point))
            return false;

        Point p = (Point) ob;

        return x == p.x && y == p.y;
    }
}