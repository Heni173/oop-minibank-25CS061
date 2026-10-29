import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Task " + taskId + " is running on "
                            + Thread.currentThread().getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        pool.shutdown();

        try {
            if (pool.awaitTermination(1, TimeUnit.MINUTES)) {
                System.out.println("All tasks completed.");
            } else {
                System.out.println("Timeout: some tasks are still running.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
