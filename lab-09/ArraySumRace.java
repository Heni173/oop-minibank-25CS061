import java.util.concurrent.atomic.AtomicLong;

class SharedTotal
{
    long total = 0;

    public void add(long value)
    {
        long old = total;

        Thread.yield();

        total = old + value;
    }
}

class SynchronizedTotal
{
    long total = 0;

    public synchronized void add(long value)
    {
        total = total + value;
    }
}

class AtomicTotal
{
    AtomicLong total = new AtomicLong(0);

    public void add(long value)
    {
        total.addAndGet(value);
    }
}

public class ArraySumRace
{
    static int[] numbers = new int[100000];

    public static void main(String a[]) throws InterruptedException
    {
        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i] = 1;
        }

        System.out.println("Expected total: " + numbers.length);

        testWithoutSynchronization();
        testWithSynchronized();
        testWithAtomic();
    }

    public static void testWithoutSynchronization() throws InterruptedException
    {
        SharedTotal obj = new SharedTotal();

        long start = System.nanoTime();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 25000; i++)
                obj.add(numbers[i]);
        });

        Thread t2 = new Thread(() -> {
            for (int i = 25000; i < 50000; i++)
                obj.add(numbers[i]);
        });

        Thread t3 = new Thread(() -> {
            for (int i = 50000; i < 75000; i++)
                obj.add(numbers[i]);
        });

        Thread t4 = new Thread(() -> {
            for (int i = 75000; i < 100000; i++)
                obj.add(numbers[i]);
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        long end = System.nanoTime();

        System.out.println();
        System.out.println("Without Synchronization");
        System.out.println("Total: " + obj.total);
        System.out.println("Time: " + (end - start) + " ns");
    }

    public static void testWithSynchronized() throws InterruptedException
    {
        SynchronizedTotal obj = new SynchronizedTotal();

        long start = System.nanoTime();

        Thread t1 = new Thread(() -> {for (int i = 0; i < 25000; i++)obj.add(numbers[i]); });
        Thread t2 = new Thread(() -> {for (int i = 25000; i < 50000; i++)obj.add(numbers[i]);});
        Thread t3 = new Thread(() -> {for (int i = 50000; i < 75000; i++) obj.add(numbers[i]);});
        Thread t4 = new Thread(() -> {for (int i = 75000; i < 100000; i++)obj.add(numbers[i]);});

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        long end = System.nanoTime();

        System.out.println();
        System.out.println("With Synchronized");
        System.out.println("Total: " + obj.total);
        System.out.println("Time: " + (end - start) + " ns");
    }

    public static void testWithAtomic() throws InterruptedException
    {
        AtomicTotal obj = new AtomicTotal();

        long start = System.nanoTime();
        Thread t1 = new Thread(() -> {for (int i = 0; i < 25000; i++)obj.add(numbers[i]); });
        Thread t2 = new Thread(() -> {for (int i = 25000; i < 50000; i++)obj.add(numbers[i]);});
        Thread t3 = new Thread(() -> {for (int i = 50000; i < 75000; i++)obj.add(numbers[i]);});
        Thread t4 = new Thread(() -> {for (int i = 75000; i < 100000; i++)obj.add(numbers[i]);});

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        long end = System.nanoTime();

        System.out.println();
        System.out.println("With AtomicLong");
        System.out.println("Total: " + obj.total);
        System.out.println("Time: " + (end - start) + " ns");
    }
}