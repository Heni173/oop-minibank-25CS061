package service;

import java.util.List;

import exception.BankException;
import model.Account;
import model.Command;

// Defines the operations that can be performed in the MiniBank system.
public interface BankOperations
{
    // Opens a new bank account with the given details.
    // Returns the created account.
    public Account openAccount(String type, String ownerName, String mobile, long openingBalance) throws BankException;

    // Deposits the specified amount into the given account.
    public void deposit(String accountNumber, long amount) throws BankException;

    // Withdraws the specified amount from the given account.
    public void withdraw(String accountNumber, long amount) throws BankException;

    // Transfers the specified amount from one account to another.
    public void transfer(String fromNumber, String toNumber, long amount) throws BankException;

    // Generates and returns the statement of the specified account.
    public String statement(String accountNumber) throws BankException;

    // Returns a list of all bank accounts.
    public List<Account> listAccounts();

    // Processes a batch of banking commands.
    public String processBatch(List<Command> commands) throws BankException;

    // Generates and returns the bank transaction report.
    public List<String> report();

    // Loads previously saved accounts and returns the number of accounts loaded.
    public int loadAccounts();

    // Saves the current account data for future use.
    public void saveAccounts();
}