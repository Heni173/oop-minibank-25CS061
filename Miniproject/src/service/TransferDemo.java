package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;

// Demonstrates a deadlock (two threads locking accounts in opposite order) and how to fix it
public class TransferDemo
{
    // Sleep helper, the delay gives the other thread time to take its lock
    static void pause(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException e)
        {
            System.out.println("Pause was interrupted");
        }
    }

    // The actual money transfer. The caller must already hold the locks of both accounts.
    static void move(Account from, Account to, long amount)
    {
        String name = Thread.currentThread().getName();

        try
        {
            from.withdraw(amount);
            to.deposit(amount);

            System.out.println(
                name + " moved " + amount + " from " +
                from.getAccountNumber() + " to " + to.getAccountNumber()
            );
        }
        catch(BankException e)
        {
            System.out.println(name + " failed : " + e.getMessage());
        }
    }

    // Locks "from" first, then "to". Two threads transferring in opposite directions
    // can each hold one lock and wait for the other forever (deadlock).
    static void unsafeTransfer(Account from, Account to, long amount)
    {
        String name = Thread.currentThread().getName();

        synchronized(from)
        {
            System.out.println(name + " locked " + from.getAccountNumber());

            // Gives the other thread time to lock its first account, which makes the deadlock certain
            pause(200);

            System.out.println(name + " waiting for " + to.getAccountNumber());

            synchronized(to)
            {
                move(from, to, amount);
            }
        }
    }

    // Always locks the account with the smaller account number first, whatever the direction.
    // Both threads now take locks in the same order, so a deadlock cannot happen.
    static void safeTransfer(Account from, Account to, long amount)
    {
        String name = Thread.currentThread().getName();

        Account first = from;
        Account second = to;

        // Swap if "from" has the bigger account number
        if(from.getAccountNumber().compareTo(to.getAccountNumber()) > 0)
        {
            first = to;
            second = from;
        }

        synchronized(first)
        {
            System.out.println(name + " locked " + first.getAccountNumber());

            pause(200);

            System.out.println(name + " waiting for " + second.getAccountNumber());

            synchronized(second)
            {
                // The transfer direction stays the same, only the locking order changed
                move(from, to, amount);
            }
        }
    }

    // safe = false : opposite lock order (deadlock), safe = true : fixed lock order
    public static void runDemo(boolean safe)
    {
        Account a = new SavingsAccount("A", 5000, 0);
        Account b = new SavingsAccount("B", 5000, 0);

        // T1 moves A to B
        Thread t1 = new Thread(() ->
        {
            if(safe)
            {
                safeTransfer(a, b, 500);
            }
            else
            {
                unsafeTransfer(a, b, 500);
            }
        }, "T1 (A to B)");

        // T2 moves B to A, the opposite direction
        Thread t2 = new Thread(() ->
        {
            if(safe)
            {
                safeTransfer(b, a, 200);
            }
            else
            {
                unsafeTransfer(b, a, 200);
            }
        }, "T2 (B to A)");

        // Daemon threads do not keep the program alive, so a deadlocked thread
        // will not stop the program from exiting
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        // Wait at most 2 seconds for each thread, instead of waiting forever
        try
        {
            t1.join(2000);
            t2.join(2000);
        }
        catch(InterruptedException e)
        {
            System.out.println("Main thread was interrupted");
        }

        // A thread that is still alive after the timeout is stuck, which means deadlock
        if(t1.isAlive() || t2.isAlive())
        {
            System.out.println(">>> DEADLOCK : both threads are stuck");
            System.out.println(t1.getName() + " state : " + t1.getState());
            System.out.println(t2.getName() + " state : " + t2.getState());
        }
        else
        {
            System.out.println(
                ">>> Finished. Balances : A = " + a.getBalance() +
                ", B = " + b.getBalance()
            );
        }
    }
}