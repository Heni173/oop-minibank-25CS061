class Cinema
{
    int seatsLeft = 5;
    int seatsBooked = 0;

    public synchronized void book()
    {
        if (seatsLeft > 0)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }

            seatsLeft--;
            seatsBooked++;

            System.out.println(Thread.currentThread().getName() + " booked a seat");
        }
    }
}

public class SeatBookingRace
{
    public static void main(String a[])
    {
        Cinema c1 = new Cinema();
        Thread t1 = new Thread(() -> {for (int i = 0; i < 5; i++)c1.book();}, "Thread-1");
        Thread t2 = new Thread(() -> {for (int i = 0; i < 5; i++)c1.book();}, "Thread-2");

        t1.start();
        t2.start();

        try
        {
            t1.join();
            t2.join();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        System.out.println("Seats booked: " + c1.seatsBooked);
        System.out.println("Seats left: " + c1.seatsLeft);
    }
}