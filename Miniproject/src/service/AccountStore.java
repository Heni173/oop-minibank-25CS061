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
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> history = new ArrayList<>();

    public void add(Account account)
    {
        accounts.put(account.getAccountNumber(), account);

        history.add(
            "ADDED " + account.getAccountNumber() +
            " (" + account.getOwnerName() + ")"
        );
    }

    public Account get(String accountNumber) throws AccountNotFoundException
    {
        Account account = accounts.get(accountNumber);

        if(account == null)
        {
            throw new AccountNotFoundException(accountNumber);
        }

        return account;
    }

    public List<Account> getAll()
    {
        return new ArrayList<>(accounts.values());
    }

    public int size()
    {
        return accounts.size();
    }

    public void deposit(String accountNumber, long amount) throws BankException
    {
        Account account = get(accountNumber);

        account.deposit(amount);

        history.add("DEPOSIT " + accountNumber + " " + amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException
    {
        Account account = get(accountNumber);

        account.withdraw(amount);

        history.add("WITHDRAW " + accountNumber + " " + amount);
    }

    public List<String> getHistory()
    {
        return new ArrayList<>(history);
    }

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