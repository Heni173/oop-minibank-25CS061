package model;

import java.io.Serializable;

// Model class: one bank account (Serializable, so it can be saved in a file)
public class Account implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int accountNo;
    private String name;
    private double balance;

    public Account(int accountNo, String name, double balance)
    {
        this.accountNo = accountNo;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountNo()
    {
        return accountNo;
    }

    public String getName()
    {
        return name;
    }

    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    public String toString()
    {
        return accountNo + " | " + name + " | Rs. " + balance;
    }
}