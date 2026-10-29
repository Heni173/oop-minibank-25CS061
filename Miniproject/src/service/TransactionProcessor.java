package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor
{
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public void submit(Runnable task)
    {
        executor.execute(task);
    }

    public void stop()
    {
        executor.shutdown();

        try
        {
            if(!executor.awaitTermination(1, TimeUnit.MINUTES))
            {
                System.out.println("Timeout : some tasks are still running");
                executor.shutdownNow();
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Waiting for the pool was interrupted");
            executor.shutdownNow();
        }
    }
}