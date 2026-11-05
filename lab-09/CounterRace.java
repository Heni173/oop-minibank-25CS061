// Program: Race condition on a shared counter, then fixed using synchronized

class Counter
{
    private int count = 0;

    // Without synchronization: threads can overwrite each other's updates
    public void unsafeIncrement()
    {
        count++;
    }

    // With synchronized: only one thread can run this at a time
    public synchronized void safeIncrement()
    {
        count++;
    }

    public int getCount()
    {
        return count;
    }
}

public class CounterRace
{
    static final int THREADS = 4;        // number of threads
    static final int TIMES = 100000;     // increments done by each thread

    // Runs all threads on ONE shared counter and returns the final count
    static int runTest(boolean safe)
    {
        Counter counter = new Counter();   // shared by all threads

        Thread[] threads = new Thread[THREADS];

        // Creating the threads
        for (int i = 0; i < THREADS; i++)
        {
            threads[i] = new Thread(() ->
            {
                for (int j = 0; j < TIMES; j++)
                {
                    if (safe)
                    {
                        counter.safeIncrement();
                    }
                    else
                    {
                        counter.unsafeIncrement();
                    }
                }
            });
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

        return counter.getCount();
    }

    public static void main(String a[])
    {
        int expected = THREADS * TIMES;

        System.out.println("Expected total = " + expected);

        // Part 1: without synchronization (3 runs)
        System.out.println();
        System.out.println("Without synchronization:");
        for (int run = 1; run <= 3; run++)
        {
            System.out.println("Run " + run + " : count = " + runTest(false));
        }

        // Part 2: with synchronized (3 runs)
        System.out.println();
        System.out.println("With synchronized:");
        for (int run = 1; run <= 3; run++)
        {
            System.out.println("Run " + run + " : count = " + runTest(true));
        }
    }
}