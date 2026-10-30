package model;

import java.io.Serializable;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account>
{
    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;
    @MaxLength(10)
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    private static int accountCounter = 1;

    private static synchronized String generateAccountNumber()
    {
        return String.format("AC%04d", accountCounter++);
    }

    public static synchronized void setNextAccountNumber(int next)
    {
        if(next > accountCounter)
        {
            accountCounter = next;
        }
    }

    public Account(String ownerName, long balance)
    {
        accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException
    {
        if(amount <= 0)
        {
            throw new InvalidAmountException(
                "Deposit amount must be greater than zero : " + amount
            );
        }

        balance = balance + amount;
    }

    @Override
    public synchronized void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException
    {
        if(amount <= 0)
        {
            throw new InvalidAmountException(
                "Withdrawal amount must be greater than zero : " + amount
            );
        }

        if(!canWithdraw(amount))
        {
            long shortfall = amount - availableBalance();

            throw new InsufficientFundsException(
                "Insufficient funds in " + accountNumber + ", short by " + shortfall,
                shortfall
            );
        }

        balance = balance - amount;
    }

    public void transfer(Account to, long amount) throws BankException
    {
        if(to == null)
        {
            throw new AccountNotFoundException("destination account");
        }

        long balanceBefore = balance;

        try
        {
            withdraw(amount);
            to.deposit(amount);

            System.out.println(
                "Transfer of " + amount + " from " + accountNumber +
                " to " + to.getAccountNumber() + " successful"
            );
        }
        catch(BankException e)
        {
            balance = balanceBefore;

            System.out.println(
                "Transfer failed, balance restored : " + e.getMessage()
            );

            throw e;
        }
        finally
        {
            System.out.println(
                "Transfer attempt finished for " + accountNumber
            );
        }
    }

    protected long availableBalance()
    {
        return balance;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    @Override
    public long getBalance()
    {
        return balance;
    }

    public boolean getActive()
    {
        return active;
    }

    @Override
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    @Override
    public int compareTo(Account other)
    {
        return accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString()
    {
        return "Account Number : " + accountNumber +
               ", Owner Name : " + ownerName +
               ", Balance : " + balance;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }

        if(!(o instanceof Account))
        {
            return false;
        }

        Account other = (Account)o;

        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode()
    {
        return accountNumber.hashCode();
    }
}