class CinemaShow
{
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    public CinemaShow(String title, int capacity)
    {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    public CinemaShow(String title)
    {
        this(title, 100);
    }

    public boolean book(int n)
    {
        if (n <= seatsAvailable)
        {
            seatsAvailable = seatsAvailable - n;
            totalBooked = totalBooked + n;

            System.out.println("Congratulations...... " + n + " seats booked");

            return true;
        }
        else
        {
            System.out.println("Booking Failed");
            return false;
        }
    }

    public void cancel(int n)
    {
        if ((seatsAvailable + n) > capacity)
        {
            System.out.println("Cannot cancel seats beyond the capacity");
            return;
        }
        else
        {
            seatsAvailable = seatsAvailable + n;
            System.out.println(n + " seats cancelled successfully");
        }
    }

    public int getSeatsAvailable()
    {
        return seatsAvailable;
    }

    public static int getTotalBooked()
    {
        return totalBooked;
    }

    public static void main(String args[])
    {
        CinemaShow c1 = new CinemaShow("Monster", 200);
        CinemaShow c2 = new CinemaShow("Avengers");

        System.out.println("C1 Available Seats = " + c1.getSeatsAvailable());
        System.out.println("C2 Available Seats = " + c2.getSeatsAvailable());

        c2.book(25);
        c1.book(76);

        System.out.println("C1 Available Seats = " + c1.getSeatsAvailable());
        System.out.println("C2 Available Seats = " + c2.getSeatsAvailable());

        c2.book(200);
        c1.cancel(202);
        c1.cancel(2);

        System.out.println("C1 Available Seats = " + c1.getSeatsAvailable());
        System.out.println("C2 Available Seats = " + c2.getSeatsAvailable());

        System.out.println("Total Booked Seats = " + CinemaShow.getTotalBooked());
    }
}