// Program: CinemaShow class using static members, final variable and constructor chaining

class CinemaShow
{
    // Instance variables
    private String title;
    private int seatsAvailable;
    private final int capacity;       // capacity cannot change once set

    // Static variable to count total seats booked in all shows
    private static int totalBooked = 0;

    // Constructor with title and capacity
    public CinemaShow(String title, int capacity)
    {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;   // initially all seats are available
    }

    // Constructor with only title (default capacity 100)
    public CinemaShow(String title)
    {
        this(title, 100);   // calling the other constructor
    }

    // Book n seats if available, return true on success else false
    public boolean book(int n)
    {
        if (n <= seatsAvailable)
        {
            seatsAvailable = seatsAvailable - n;
            totalBooked = totalBooked + n;
            return true;
        }
        return false;   // seats remain unchanged
    }

    // Cancel n seats but seats should never go above capacity
    public void cancel(int n)
    {
        seatsAvailable = Math.min(seatsAvailable + n, capacity);
    }

    // Getter for available seats
    public int getSeatsAvailable()
    {
        return seatsAvailable;
    }

    // Static method to get total booked seats
    public static int getTotalBooked()
    {
        return totalBooked;
    }

    public static void main(String args[])
    {
        // Creating two shows
        CinemaShow c1 = new CinemaShow("Monster", 200);
        CinemaShow c2 = new CinemaShow("Avengers");

        System.out.println("Monster capacity = 200, Avengers capacity = 100");
        System.out.println();

        // Successful booking
        boolean r = c1.book(50);
        System.out.println("Monster book(50)   : " + r + " | Seats = " + c1.getSeatsAvailable());

        // Booking more than available (should print false, seats unchanged)
        r = c1.book(160);
        System.out.println("Monster book(160)  : " + r + " | Seats = " + c1.getSeatsAvailable());

        r = c2.book(30);
        System.out.println("Avengers book(30)  : " + r + " | Seats = " + c2.getSeatsAvailable());

        r = c2.book(100);
        System.out.println("Avengers book(100) : " + r + " | Seats = " + c2.getSeatsAvailable());

        // Cancel within limit
        c1.cancel(20);
        System.out.println("Monster cancel(20) : Seats = " + c1.getSeatsAvailable());

        // Cancel more than capacity (seats should stop at capacity)
        c1.cancel(100);
        System.out.println("Monster cancel(100): Seats = " + c1.getSeatsAvailable());

        c2.cancel(10);
        System.out.println("Avengers cancel(10): Seats = " + c2.getSeatsAvailable());

        // Total seats booked successfully
        System.out.println();
        System.out.println("Total Booked = " + CinemaShow.getTotalBooked());
    }
}