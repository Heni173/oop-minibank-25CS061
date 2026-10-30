package service;

import java.util.List;

import exception.BankException;
import model.Account;
import model.Command;

public interface BankOperations
{
    Account openAccount(String type, String ownerName, String mobile, long openingBalance) throws BankException;

    void deposit(String accountNumber, long amount) throws BankException;

    void withdraw(String accountNumber, long amount) throws BankException;

    void transfer(String fromNumber, String toNumber, long amount) throws BankException;

    String statement(String accountNumber) throws BankException;

    List<Account> listAccounts();

    String processBatch(List<Command> commands) throws BankException;

    List<String> report();

    int loadAccounts();

    void saveAccounts();
}