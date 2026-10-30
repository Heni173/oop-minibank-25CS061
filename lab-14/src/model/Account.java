package model;

import java.util.Objects;

public class Account {

    private final int id;
    private final String holderName;
    private double balance;

    public Account(int id, String holderName, double balance) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Account)) {
            return false;
        }
        Account other = (Account) o;
        return id == other.id
                && holderName.equals(other.holderName)
                && Double.compare(balance, other.balance) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, holderName, balance);
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", holder=" + holderName + ", balance=" + balance + "}";
    }
}