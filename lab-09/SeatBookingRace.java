// Program: Seat booking race condition, then fixed using synchronized

class Cinema
{
    private int seatsLeft = 5;      // shared by all threads
    private int seatsBooked = 0;

    // Common booking logic: check the seat, then book it
    private void bookSeat()
    {
        String name = Thread.currentThread().getName();

        if (seatsLeft > 0)
        {
            // Small delay between checking and booking, so other threads
            // also pass the check before this thread reduces the seats
            try
            {
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            seatsLeft--;
            seatsBooked++;

            System.out.println(name + " booked a seat");
        }
        else
        {
            System.out.println(name + " could not book (sold out)");
        }
    }

    // Without synchronization: many threads can enter together
    public void unsafeBook()
    {
        bookSeat();
    }

    // With synchronized: only one thread can enter at a time
    public synchronized void safeBook()
    {
        bookSeat();
    }

    public int getSeatsLeft()
    {
        return seatsLeft;
    }

    public int getSeatsBooked()
    {
        return seatsBooked;
    }
}

public class SeatBookingRace
{
    static final int THREADS = 10;     // number of customers

    // Runs 10 threads, each tries to book one seat
    static void runTest(boolean safe)
    {
        Cinema c1 = new Cinema();      // ONE cinema shared by all threads

        Thread[] threads = new Thread[THREADS];

        // Creating the threads
        for (int i = 0; i < THREADS; i++)
        {
            String name = "Customer-" + (i + 1);

            threads[i] = new Thread(() ->
            {
                if (safe)
                {
                    c1.safeBook();
                }
                else
                {
                    c1.unsafeBook();
                }
            }, name);
        }

        // Starting all threads
        for (int i = 0; i < THREADS; i++)
        {
            threads[i].start();
        }

        // Waiting for all threads to finish
        try
        {
            for (int i = 0; i < THREADS; i++)
            {
                threads[i].join();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        System.out.println("Seats booked: " + c1.getSeatsBooked());
        System.out.println("Seats left: " + c1.getSeatsLeft());

        if (c1.getSeatsBooked() > 5)
        {
            System.out.println("Oversold! More than 5 seats were booked.");
        }
        else
        {
            System.out.println("Correct! Exactly 5 seats were booked.");
        }
    }

    public static void main(String a[])
    {
        System.out.println("Without synchronization:");
        runTest(false);

        System.out.println();
        System.out.println("With synchronized:");
        runTest(true);
    }
}