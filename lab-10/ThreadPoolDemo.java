// Program: Thread pool runner using ExecutorService (fixed pool of 3 threads)

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {

    public static void main(String[] args) {

        // Creating a pool with only 3 threads
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // Submitting 10 tasks to the pool
        for (int i = 1; i <= 10; i++) {

            final int taskId = i;   // final, so the task can use it

            pool.submit(new Runnable() {

                @Override
                public void run() {

                    // Printing task id and the name of the thread running it
                    System.out.println("Task " + taskId + " is running on "
                            + Thread.currentThread().getName());

                    // Sleeping for a short time to show that the task takes time
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                    System.out.println("Task " + taskId + " finished on "
                            + Thread.currentThread().getName());
                }
            });
        }

        // No new tasks will be accepted, the submitted tasks will still finish
        pool.shutdown();

        // Waiting for all tasks to finish (maximum 1 minute)
        try {
            if (pool.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Timeout: some tasks are still running.");
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}