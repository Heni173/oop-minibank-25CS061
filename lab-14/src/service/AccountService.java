package service;

import java.util.ArrayList;
import model.Account;

// Service class: all the bank rules, using an ArrayList
public class AccountService
{
    private ArrayList<Account> accounts = new ArrayList<Account>();
    private int nextNumber = 1001;      // account number of the next new account

    // Empty bank
    public AccountService()
    {
    }

    // Bank started with accounts that were loaded from the file
    public AccountService(ArrayList<Account> existing)
    {
        accounts = existing;

        // Next number must be bigger than every existing number
        for (Account a : accounts)
        {
            if (a.getAccountNo() >= nextNumber)
            {
                nextNumber = a.getAccountNo() + 1;
            }
        }
    }

    // Adds a new account, throws an exception for invalid data
    public Account addAccount(String name, double openingBalance)
    {
        if (name == null || name.trim().isEmpty())
        {
            throw new IllegalArgumentException("Name must not be blank");
        }

        if (openingBalance < 0)
        {
            throw new IllegalArgumentException("Opening balance must not be negative");
        }

        Account acc = new Account(nextNumber, name.trim(), openingBalance);
        nextNumber++;
        accounts.add(acc);

        return acc;
    }

    // Adds money to an account
    public void deposit(int accountNo, double amount)
    {
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account acc = find(accountNo);
        acc.setBalance(acc.getBalance() + amount);
    }

    // Takes money out of an account
    public void withdraw(int accountNo, double amount)
    {
        if (amount <= 0)
        {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        Account acc = find(accountNo);

        if (amount > acc.getBalance())
        {
            throw new IllegalArgumentException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
    }

    // Finds an account by its number, throws an exception if it is not there
    public Account find(int accountNo)
    {
        for (Account a : accounts)
        {
            if (a.getAccountNo() == accountNo)
            {
                return a;
            }
        }

        throw new IllegalArgumentException("Account not found: " + accountNo);
    }

    public int getCount()
    {
        return accounts.size();
    }

    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }
}