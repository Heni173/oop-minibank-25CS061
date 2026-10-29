class Buffer {
    private int value;
    private boolean available = false;

    public synchronized void produce(int number) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        value = number;
        available = true;

        System.out.println("Produced: " + number);

        notify();
    }

    public synchronized void consume() {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Consumed: " + value);

        available = false;

        notify();
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        Runnable producerTask = new Runnable() {
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    buffer.produce(i);
                }
            }
        };

        Runnable consumerTask = new Runnable() {
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    buffer.consume();
                }
            }
        };

        Thread producer = new Thread(producerTask);
        Thread consumer = new Thread(consumerTask);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All items produced and consumed.");
    }
}