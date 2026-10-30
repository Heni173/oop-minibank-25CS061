package service;

import model.Account;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountService {

    private final List<Account> accounts = new ArrayList<>();
    private int nextId = 1;

    public Account addAccount(String holderName, double openingBalance) {
        if (holderName == null || holderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Holder name must not be blank");
        }
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance must not be negative");
        }
        Account account = new Account(nextId++, holderName.trim(), openingBalance);
        accounts.add(account);
        return account;
    }

    public int count() {
        return accounts.size();
    }

    public List<Account> getAll() {
        return Collections.unmodifiableList(accounts);
    }

    public Account findById(int id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        throw new IllegalArgumentException("Account not found: " + id);
    }

    public void deposit(int id, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        findById(id).deposit(amount);
    }

    public void replaceAll(List<Account> loaded) {
        accounts.clear();
        accounts.addAll(loaded);
        nextId = 1;
        for (Account account : loaded) {
            if (account.getId() >= nextId) {
                nextId = account.getId() + 1;
            }
        }
    }
}