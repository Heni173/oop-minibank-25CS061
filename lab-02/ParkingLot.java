// Program: ParkingLot class using final variables and static revenue

class ParkingLot
{
    // Instance variables for current vehicles
    private int twoWheelers;
    private int fourWheelers;

    // Capacity of each section (cannot change once set)
    private final int twoCap;
    private final int fourCap;

    // Static variable to store total revenue
    private static long revenue = 0;

    // Constructor to set capacities
    public ParkingLot(int twoCap, int fourCap)
    {
        this.twoCap = twoCap;
        this.fourCap = fourCap;
        twoWheelers = 0;
        fourWheelers = 0;
    }

    // Park a vehicle of given type ("two" or "four")
    public void park(String type)
    {
        if (type.equals("two"))
        {
            if (twoWheelers < twoCap)
            {
                twoWheelers++;
                revenue += 20;               // fee for two wheeler
                System.out.println("Two wheeler parked. Fee = 20");
            }
            else
            {
                System.out.println("Full");  // section is full, reject
            }
        }
        else if (type.equals("four"))
        {
            if (fourWheelers < fourCap)
            {
                fourWheelers++;
                revenue += 40;               // fee for four wheeler
                System.out.println("Four wheeler parked. Fee = 40");
            }
            else
            {
                System.out.println("Full");  // section is full, reject
            }
        }
        else
        {
            System.out.println("Invalid vehicle type");
        }
    }

    // Vehicle leaves, count should never go below 0
    public void leave(String type)
    {
        if (type.equals("two"))
        {
            if (twoWheelers > 0)
            {
                twoWheelers--;
                System.out.println("Two wheeler left");
            }
            else
            {
                System.out.println("No two wheeler to leave");
            }
        }
        else if (type.equals("four"))
        {
            if (fourWheelers > 0)
            {
                fourWheelers--;
                System.out.println("Four wheeler left");
            }
            else
            {
                System.out.println("No four wheeler to leave");
            }
        }
        else
        {
            System.out.println("Invalid vehicle type");
        }
    }

    // Getters
    public int getTwoWheelers()
    {
        return twoWheelers;
    }

    public int getFourWheelers()
    {
        return fourWheelers;
    }

    public static long getRevenue()
    {
        return revenue;
    }

    public static void main(String args[])
    {
        // Parking lot with 2 two-wheeler slots and 2 four-wheeler slots
        ParkingLot p = new ParkingLot(2, 2);

        // Filling the two wheeler section (third one should be refused)
        p.park("two");
        p.park("two");
        p.park("two");

        // Filling the four wheeler section (third one should be refused)
        p.park("four");
        p.park("four");
        p.park("four");

        // Some vehicles leave
        p.leave("two");
        p.leave("four");

        // Parking again after space is free
        p.park("two");
        p.park("four");

        // Parking into full sections again
        p.park("two");
        p.park("four");

        // Everyone leaves, extra leave should not make count negative
        p.leave("two");
        p.leave("two");
        p.leave("two");
        p.leave("four");

        // Final occupancy and revenue
        System.out.println();
        System.out.println("Two Wheelers = " + p.getTwoWheelers());
        System.out.println("Four Wheelers = " + p.getFourWheelers());
        System.out.println("Revenue = " + ParkingLot.getRevenue());
    }
}