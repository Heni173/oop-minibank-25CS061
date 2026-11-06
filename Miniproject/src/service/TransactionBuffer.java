package service;

import model.Command;

// Fixed size circular buffer shared by a producer thread and a consumer thread
public class TransactionBuffer
{
    private final Command[] items;
    private final Object lock = new Object();

    // count = items currently in the buffer
    // putIndex = next slot to write, takeIndex = next slot to read
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;

    public TransactionBuffer(int capacity)
    {
        items = new Command[capacity];
    }

    // Called by the producer. Waits while the buffer is full.
    public void put(Command command) throws InterruptedException
    {
        synchronized(lock)
        {
            // while (not if) : re-check the condition after every wake up
            while(count == items.length)
            {
                System.out.println("Buffer full, producer waiting");
                lock.wait();
            }

            items[putIndex] = command;

            // % wraps the index back to 0 after the last slot (circular)
            putIndex = (putIndex + 1) % items.length;
            count++;

            // Wake up the waiting consumer, there is something to take now
            lock.notify();
        }
    }

    // Called by the consumer. Waits while the buffer is empty.
    public Command take() throws InterruptedException
    {
        synchronized(lock)
        {
            while(count == 0)
            {
                System.out.println("Buffer empty, consumer waiting");
                lock.wait();
            }

            Command command = items[takeIndex];

            // Clear the slot so the old object can be garbage collected
            items[takeIndex] = null;
            takeIndex = (takeIndex + 1) % items.length;
            count--;

            // Wake up the waiting producer, there is free space now
            lock.notify();

            return command;
        }
    }
}