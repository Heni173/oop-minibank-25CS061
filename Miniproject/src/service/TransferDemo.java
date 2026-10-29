package service;

import exception.BankException;
import model.Account;
import model.SavingsAccount;

public class TransferDemo
{
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

    static void unsafeTransfer(Account from, Account to, long amount)
    {
        String name = Thread.currentThread().getName();

        synchronized(from)
        {
            System.out.println(name + " locked " + from.getAccountNumber());

            pause(200);

            System.out.println(name + " waiting for " + to.getAccountNumber());

            synchronized(to)
            {
                move(from, to, amount);
            }
        }
    }

    static void safeTransfer(Account from, Account to, long amount)
    {
        String name = Thread.currentThread().getName();

        Account first = from;
        Account second = to;

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
                move(from, to, amount);
            }
        }
    }

    public static void runDemo(boolean safe)
    {
        Account a = new SavingsAccount("A", 5000, 0);
        Account b = new SavingsAccount("B", 5000, 0);

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

        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        try
        {
            t1.join(2000);
            t2.join(2000);
        }
        catch(InterruptedException e)
        {
            System.out.println("Main thread was interrupted");
        }

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