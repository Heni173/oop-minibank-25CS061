// Program: Counting distinct points using equals()
// PointDemo is the Driver class of this program

public class PointDemo
{
    public static void main(String[] args)
    {
        // 5 points with 2 repeated coordinates
        Point[] points =
        {
            new Point(1, 2),
            new Point(3, 4),
            new Point(1, 2),   // repeat of first
            new Point(5, 6),
            new Point(3, 4)    // repeat of second
        };

        // Printing all points using toString()
        System.out.println("Points:");
        for (int i = 0; i < points.length; i++)
        {
            System.out.println(points[i]);
        }

        int distinct = 0;

        // Check each point against the earlier ones
        for (int i = 0; i < points.length; i++)
        {
            boolean alreadyAppeared = false;

            for (int j = 0; j < i; j++)
            {
                if (points[i].equals(points[j]))
                {
                    alreadyAppeared = true;
                    break;
                }
            }

            // Count only if it did not appear earlier
            if (!alreadyAppeared)
            {
                distinct++;
            }
        }

        System.out.println("Distinct: " + distinct);
    }
}