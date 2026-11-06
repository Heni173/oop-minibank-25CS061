package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exception.AccountNotFoundException;
import exception.BankException;
import model.Account;

public class AccountStore
{
    // Stores all accounts using account number as the key.
    private final Map<String, Account> accounts = new HashMap<>();

    // Stores the history of account-related operations.
    private final List<String> history = new ArrayList<>();

    // Adds a new account to the store and records its details in history.
    public void add(Account account)
    {
        accounts.put(account.getAccountNumber(), account);

        history.add(
            "ADDED " + account.getAccountNumber() +
            " (" + account.getOwnerName() + ")"
        );
    }

    // Returns an account using its account number.
    // Throws AccountNotFoundException if the account does not exist.
    public Account get(String accountNumber) throws AccountNotFoundException
    {
        Account account = accounts.get(accountNumber);

        if(account == null)
        {
            throw new AccountNotFoundException(accountNumber);
        }

        return account;
    }

    // Returns a new list containing all stored accounts.
    public List<Account> getAll()
    {
        return new ArrayList<>(accounts.values());
    }

    // Returns the total number of accounts stored.
    public int size()
    {
        return accounts.size();
    }

    // Deposits the given amount into the specified account.
    // Records the deposit operation in transaction history.
    public void deposit(String accountNumber, long amount) throws BankException
    {
        Account account = get(accountNumber);

        account.deposit(amount);

        history.add("DEPOSIT " + accountNumber + " " + amount);
    }

    // Withdraws the given amount from the specified account.
    // Records the withdrawal operation in transaction history.
    public void withdraw(String accountNumber, long amount) throws BankException
    {
        Account account = get(accountNumber);

        account.withdraw(amount);

        history.add("WITHDRAW " + accountNumber + " " + amount);
    }

    // Returns a new list containing all transaction history records.
    public List<String> getHistory()
    {
        return new ArrayList<>(history);
    }

    // Returns accounts sorted by balance in descending order.
    // If balances are equal, sorts accounts by account number.
    public ArrayList<Account> sortedByBalance()
    {
        ArrayList<Account> list = new ArrayList<>(accounts.values());

        list.sort((a, b) ->
        {
            int result = Long.compare(b.getBalance(), a.getBalance());

            if(result != 0)
            {
                return result;
            }

            return a.compareTo(b);
        });

        return list;
    }
}