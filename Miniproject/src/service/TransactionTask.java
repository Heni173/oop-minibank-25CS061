package service;

import exception.BankException;
import model.Account;
import model.TransactionType;

// One deposit or withdraw job, written as a Runnable so it can be run by a thread pool
public class TransactionTask implements Runnable
{
    // id is only used to identify the task in the output
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
        // Name of the pool thread that is running this task
        String name = Thread.currentThread().getName();

        try
        {
            // Any type other than DEPOSIT is treated as a withdraw
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

            // Small delay to simulate work, so different threads pick up tasks
            Thread.sleep(50);
        }
        catch(BankException e)
        {
            // A failed transaction is only reported, it does not stop other tasks
            System.out.println(
                "Task " + id + " failed : " + e.getMessage()
            );
        }
        catch(InterruptedException e)
        {
            // Thrown by sleep() if the thread is interrupted
            System.out.println("Task " + id + " was interrupted");
        }
    }
}