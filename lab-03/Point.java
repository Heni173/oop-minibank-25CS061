// Program: Point class with toString, equals and hashCode

import java.util.Objects;

public class Point
{
    // Coordinates of the point
    private int x;
    private int y;

    // Constructor to set x and y
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    // Returns point in "(x, y)" format
    @Override
    public String toString()
    {
        return "(" + x + ", " + y + ")";
    }

    // Two points are equal when both x and y match
    @Override
    public boolean equals(Object ob)
    {
        if (this == ob)
        {
            return true;
        }

        if (!(ob instanceof Point))
        {
            return false;
        }

        Point p = (Point) ob;
        return x == p.x && y == p.y;
    }

    // hashCode must agree with equals()
    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}