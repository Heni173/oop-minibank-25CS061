package service;

import exception.BankException;
import model.Account;
import model.TransactionType;

public class TransactionTask implements Runnable
{
    private final int id;
    private final Account account;
    private final TransactionType type;
    private final long amount;

    public TransactionTask(int id, Account account, TransactionType type, long amount)
    {
        this.id = id;
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void run()
    {
        String name = Thread.currentThread().getName();

        try
        {
            if(type == TransactionType.DEPOSIT)
            {
                account.deposit(amount);
            }
            else
            {
                account.withdraw(amount);
            }

            System.out.println(
                "Task " + id + " : " + type + " " + amount +
                " done by " + name
            );

            Thread.sleep(50);
        }
        catch(BankException e)
        {
            System.out.println(
                "Task " + id + " failed : " + e.getMessage()
            );
        }
        catch(InterruptedException e)
        {
            System.out.println("Task " + id + " was interrupted");
        }
    }
}