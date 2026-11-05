// Program: Adding an array using several threads into one shared total
// Wrong result (race), then fixed with synchronized and AtomicLong, and compared

import java.util.concurrent.atomic.AtomicLong;

// Shared total with three ways of adding
class SharedTotal
{
    private long unsafeTotal = 0;                          // no protection
    private long syncTotal = 0;                            // protected by synchronized
    private AtomicLong atomicTotal = new AtomicLong(0);    // protected by AtomicLong

    // Way 1: no protection, threads can overwrite each other's updates
    public void addUnsafe(int x)
    {
        unsafeTotal = unsafeTotal + x;
    }

    // Way 2: synchronized, only one thread can add at a time
    public synchronized void addSync(int x)
    {
        syncTotal = syncTotal + x;
    }

    // Way 3: AtomicLong adds in one safe step
    public void addAtomic(int x)
    {
        atomicTotal.addAndGet(x);
    }

    // Returns the total of the chosen way (1, 2 or 3)
    public long getTotal(int mode)
    {
        if (mode == 1)
        {
            return unsafeTotal;
        }
        else if (mode == 2)
        {
            return syncTotal;
        }
        else
        {
            return atomicTotal.get();
        }
    }
}

// Each thread adds its own part of the array into the shared total
class SumThread extends Thread
{
    private int[] numbers;
    private int from;          // first index of this thread's part
    private int to;            // last index + 1
    private SharedTotal total;
    private int mode;          // 1 = unsafe, 2 = synchronized, 3 = atomic

    public SumThread(int[] numbers, int from, int to, SharedTotal total, int mode)
    {
        this.numbers = numbers;
        this.from = from;
        this.to = to;
        this.total = total;
        this.mode = mode;
    }

    public void run()
    {
        for (int i = from; i < to; i++)
        {
            if (mode == 1)
            {
                total.addUnsafe(numbers[i]);
            }
            else if (mode == 2)
            {
                total.addSync(numbers[i]);
            }
            else
            {
                total.addAtomic(numbers[i]);
            }
        }
    }
}

public class ParallelSum
{
    static final int SIZE = 2000000;    // size of the array
    static final int THREADS = 4;       // number of threads

    // Runs one test: splits the array across threads, measures time, prints result
    static void runTest(String label, int mode, int[] numbers, long expected)
    {
        SharedTotal total = new SharedTotal();     // ONE total shared by all threads
        SumThread[] threads = new SumThread[THREADS];

        int part = numbers.length / THREADS;       // size of each thread's part

        // Creating the threads, each gets its own part of the array
        for (int i = 0; i < THREADS; i++)
        {
            int from = i * part;
            int to = from + part;

            // Last thread takes the remaining numbers
            if (i == THREADS - 1)
            {
                to = numbers.length;
            }

            threads[i] = new SumThread(numbers, from, to, total, mode);
        }

        // Starting the timer
        long startTime = System.nanoTime();

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

        // Stopping the timer
        long endTime = System.nanoTime();
        double ms = (endTime - startTime) / 1000000.0;

        long result = total.getTotal(mode);
        String verdict = "Wrong";

        if (result == expected)
        {
            verdict = "Correct";
        }

        System.out.printf("%-14s total = %d | %-7s | time = %.1f ms%n",
                label, result, verdict, ms);
    }

    public static void main(String[] args)
    {
        // Filling the array with 1, 2, 3, ... SIZE
        int[] numbers = new int[SIZE];

        for (int i = 0; i < SIZE; i++)
        {
            numbers[i] = i + 1;
        }

        // Correct answer using a simple single-thread loop
        long expected = 0;

        for (int i = 0; i < SIZE; i++)
        {
            expected = expected + numbers[i];
        }

        System.out.println("Numbers = " + SIZE + ", Threads = " + THREADS);
        System.out.println("Expected total = " + expected);
        System.out.println();

        // Running the three ways one by one
        runTest("Unsafe", 1, numbers, expected);
        runTest("Synchronized", 2, numbers, expected);
        runTest("AtomicLong", 3, numbers, expected);
    }
}