package service;

import model.Command;

public class TransactionBuffer
{
    private final Command[] items;
    private final Object lock = new Object();

    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;

    public TransactionBuffer(int capacity)
    {
        items = new Command[capacity];
    }

    public void put(Command command) throws InterruptedException
    {
        synchronized(lock)
        {
            while(count == items.length)
            {
                System.out.println("Buffer full, producer waiting");
                lock.wait();
            }

            items[putIndex] = command;
            putIndex = (putIndex + 1) % items.length;
            count++;

            lock.notify();
        }
    }

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
            items[takeIndex] = null;
            takeIndex = (takeIndex + 1) % items.length;
            count--;

            lock.notify();

            return command;
        }
    }
}