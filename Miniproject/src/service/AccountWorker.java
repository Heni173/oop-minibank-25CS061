package service;

import exception.InvalidAmountException;
import model.Account;

// AccountWorker performs deposit operations in a separate thread.
public class AccountWorker implements Runnable
{
    // Account on which deposit operations will be performed.
    private Account account;

    // Number of times the deposit operation will be repeated.
    private int times;

    // Amount to be deposited in each operation.
    private long amount;

    // Constructor to initialize account, number of times and deposit amount.
    public AccountWorker(Account account, int times, long amount)
    {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    // Executes the deposit operations when the thread starts.
    @Override
    public void run()
    {
        // Gets the name of the currently running thread.
        String name = Thread.currentThread().getName();

        System.out.println(name + " started");

        // Repeats the deposit operation for the given number of times.
        for(int i = 0; i < times; i++)
        {
            try
            {
                // Deposits the specified amount into the account.
                account.deposit(amount);
            }
            catch(InvalidAmountException e)
            {
                // Displays an error message if the deposit amount is invalid.
                System.out.println(name + " error : " + e.getMessage());
            }
        }

        // Displays a message when all deposit operations are completed.
        System.out.println(name + " finished");
    }
}