// Program: Deadlock between two threads and its fix using a consistent lock order
// Scenario: transferring money between two bank accounts

// Bank account with an id and a balance
class Account
{
    int id;
    int balance;

    Account(int id, int balance)
    {
        this.id = id;
        this.balance = balance;
    }
}

public class DeadlockDemo
{
    // Small delay, so both threads get time to lock their first account
    static void pause(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            System.out.println(e);
        }
    }

    // UNSAFE: locks "from" first and then "to" (opposite order in the two threads)
    static void unsafeTransfer(Account from, Account to, int amount)
    {
        synchronized (from)
        {
            System.out.println(Thread.currentThread().getName() + " locked account " + from.id);
            pause(200);

            System.out.println(Thread.currentThread().getName() + " waiting for account " + to.id);

            synchronized (to)
            {
                from.balance = from.balance - amount;
                to.balance = to.balance + amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + amount);
            }
        }
    }

    // SAFE: always locks the account with the smaller id first
    static void safeTransfer(Account from, Account to, int amount)
    {
        Account first;
        Account second;

        // Deciding the lock order by id, same order for every thread
        if (from.id < to.id)
        {
            first = from;
            second = to;
        }
        else
        {
            first = to;
            second = from;
        }

        synchronized (first)
        {
            System.out.println(Thread.currentThread().getName() + " locked account " + first.id);
            pause(200);

            System.out.println(Thread.currentThread().getName() + " waiting for account " + second.id);

            synchronized (second)
            {
                from.balance = from.balance - amount;
                to.balance = to.balance + amount;
                System.out.println(Thread.currentThread().getName() + " transferred " + amount);
            }
        }
    }

    // Runs two opposite transfers at the same time (safe = false means unsafe version)
    static void run(boolean safe) throws InterruptedException
    {
        Account a = new Account(1, 1000);
        Account b = new Account(2, 1000);

        // Thread 1 transfers from A to B
        Thread t1 = new Thread(() ->
        {
            if (safe)
            {
                safeTransfer(a, b, 100);
            }
            else
            {
                unsafeTransfer(a, b, 100);
            }
        }, "T1 (A->B)");

        // Thread 2 transfers from B to A (opposite direction)
        Thread t2 = new Thread(() ->
        {
            if (safe)
            {
                safeTransfer(b, a, 50);
            }
            else
            {
                unsafeTransfer(b, a, 50);
            }
        }, "T2 (B->A)");

        // Daemon threads, so the program can still end if they get stuck in a deadlock
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        // Waiting at most 2 seconds for each thread
        t1.join(2000);
        t2.join(2000);

        // If a thread is still alive, it is stuck (deadlock)
        if (t1.isAlive() || t2.isAlive())
        {
            System.out.println(">>> DEADLOCK: threads are stuck waiting for each other.");
        }
        else
        {
            System.out.println(">>> Finished. Balances: A=" + a.balance + ", B=" + b.balance);
        }
    }

    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("=== Opposite lock order (deadlock) ===");
        run(false);

        System.out.println();
        System.out.println("=== Consistent lock order (fixed) ===");
        run(true);
    }
}