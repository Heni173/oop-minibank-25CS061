// Program: Producer-consumer with a small shared buffer using wait() and notify()

// Shared buffer that holds at most 3 items
class Buffer
{
    private int[] items = new int[3];   // small buffer
    private int count = 0;              // how many items are in the buffer now
    private int in = 0;                 // position where the next item is added
    private int out = 0;                // position from where the next item is removed

    // Producer calls this to add an item
    public synchronized void put(int item) throws InterruptedException
    {
        // If the buffer is full, producer waits
        while (count == items.length)
        {
            System.out.println("Buffer full, producer is waiting");
            wait();
        }

        items[in] = item;
        in = (in + 1) % items.length;    // goes back to 0 after the last position
        count++;

        System.out.println("Produced " + item + " | items in buffer = " + count);

        notifyAll();    // wake up the consumer if it was waiting
    }

    // Consumer calls this to remove an item
    public synchronized int take() throws InterruptedException
    {
        // If the buffer is empty, consumer waits
        while (count == 0)
        {
            System.out.println("Buffer empty, consumer is waiting");
            wait();
        }

        int item = items[out];
        out = (out + 1) % items.length;
        count--;

        System.out.println("Consumed " + item + " | items in buffer = " + count);

        notifyAll();    // wake up the producer if it was waiting

        return item;
    }
}

// Producer thread: adds numbers 1, 2, 3, ... to the buffer
class Producer extends Thread
{
    private Buffer buffer;
    private int total;      // how many items to produce

    public Producer(Buffer buffer, int total)
    {
        this.buffer = buffer;
        this.total = total;
    }

    public void run()
    {
        try
        {
            for (int i = 1; i <= total; i++)
            {
                buffer.put(i);
                Thread.sleep(100);    // producer is fast
            }
        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }
    }
}

// Consumer thread: removes numbers from the buffer
class Consumer extends Thread
{
    private Buffer buffer;
    private int total;              // how many items to consume
    private int consumedCount = 0;
    private boolean inOrder = true; // stays true if items come out as 1, 2, 3, ...

    public Consumer(Buffer buffer, int total)
    {
        this.buffer = buffer;
        this.total = total;
    }

    public int getConsumedCount()
    {
        return consumedCount;
    }

    public boolean isInOrder()
    {
        return inOrder;
    }

    public void run()
    {
        try
        {
            for (int i = 1; i <= total; i++)
            {
                int item = buffer.take();

                // Checking that the items come in the same order
                if (item != i)
                {
                    inOrder = false;
                }

                consumedCount++;
                Thread.sleep(300);    // consumer is slow, so the buffer fills up
            }
        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }
    }
}

public class ProducerConsumer
{
    public static void main(String[] args)
    {
        int total = 10;     // fixed number of items

        Buffer buffer = new Buffer();
        Producer producer = new Producer(buffer, total);
        Consumer consumer = new Consumer(buffer, total);

        producer.start();
        consumer.start();

        // Waiting for both threads to finish
        try
        {
            producer.join();
            consumer.join();
        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }

        System.out.println();
        System.out.println("Items produced = " + total);
        System.out.println("Items consumed = " + consumer.getConsumedCount());
        System.out.println("Order correct = " + consumer.isInOrder());
    }
}