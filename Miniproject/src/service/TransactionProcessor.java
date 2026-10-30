package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import exception.BankException;
import model.Account;
import model.Command;
import model.TransactionType;

public class TransactionProcessor
{
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Queue<Command> succeeded = new ConcurrentLinkedQueue<>();
    private final AtomicInteger failed = new AtomicInteger();

    public void register(Account account)
    {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account find(String accountNumber)
    {
        return accounts.get(accountNumber);
    }

    public int accountCount()
    {
        return accounts.size();
    }

    public void submit(Runnable task)
    {
        executor.execute(task);
    }

    public void submitTransaction(String accountNumber, TransactionType type, long amount)
    {
        executor.execute(() ->
        {
            Account account = accounts.get(accountNumber);

            if(account == null)
            {
                System.out.println("Account not found : " + accountNumber);
                failed.incrementAndGet();
                return;
            }

            try
            {
                if(type == TransactionType.DEPOSIT)
                {
                    account.deposit(amount);
                }
                else if(type == TransactionType.WITHDRAW)
                {
                    account.withdraw(amount);
                }
                else
                {
                    System.out.println("Not supported in a batch : " + type);
                    failed.incrementAndGet();
                    return;
                }

                succeeded.add(new Command(type, accountNumber, amount));
            }
            catch(BankException e)
            {
                System.out.println(
                    "Transaction failed for " + accountNumber + " : " + e.getMessage()
                );
                failed.incrementAndGet();
            }
        });
    }

    public List<Command> getSucceeded()
    {
        return new ArrayList<>(succeeded);
    }

    public int getFailedCount()
    {
        return failed.get();
    }

    public void stop()
    {
        executor.shutdown();

        try
        {
            if(!executor.awaitTermination(1, TimeUnit.MINUTES))
            {
                System.out.println("Timeout : some tasks are still running");
                executor.shutdownNow();
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Waiting for the pool was interrupted");
            executor.shutdownNow();
        }
    }
}