public class DeadlockDemo {

    static class Account {
        final int id;
        int balance;

        Account(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }
    }

    static void unsafeTransfer(Account from, Account to, int amount) {
        synchronized (from) {
            System.out.println(Thread.currentThread().getName() + " locked account " + from.id);
            pause(200);
            System.out.println(Thread.currentThread().getName() + " waiting for account " + to.id);
            synchronized (to) {
                from.balance -= amount;
                to.balance += amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + amount);
            }
        }
    }

    static void safeTransfer(Account from, Account to, int amount) {
        Account first = from.id < to.id ? from : to;
        Account second = from.id < to.id ? to : from;

        synchronized (first) {
            System.out.println(Thread.currentThread().getName() + " locked account " + first.id);
            pause(200);
            System.out.println(Thread.currentThread().getName() + " waiting for account " + second.id);
            synchronized (second) {
                from.balance -= amount;
                to.balance += amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + amount);
            }
        }
    }

    static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void run(boolean safe) throws InterruptedException {
        Account a = new Account(1, 1000);
        Account b = new Account(2, 1000);

        Thread t1 = new Thread(() -> {
            if (safe) safeTransfer(a, b, 100);
            else unsafeTransfer(a, b, 100);
        }, "T1 (A->B)");

        Thread t2 = new Thread(() -> {
            if (safe) safeTransfer(b, a, 50);
            else unsafeTransfer(b, a, 50);
        }, "T2 (B->A)");

        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();

        t1.join(2000);
        t2.join(2000);

        if (t1.isAlive() || t2.isAlive()) {
            System.out.println(">>> DEADLOCK: threads are stuck waiting for each other.");
        } else {
            System.out.println(">>> Finished. Balances: A=" + a.balance + ", B=" + b.balance);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Opposite lock order (deadlock) ===");
        run(false);

        System.out.println();
        System.out.println("=== Consistent lock order (fixed) ===");
        run(true);
    }
}
