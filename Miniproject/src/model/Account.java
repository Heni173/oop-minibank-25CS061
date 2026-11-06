package model;

import java.io.Serializable;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

// Abstract base class representing a bank account.
public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account>
{
    // Defines the unique version identifier for serialization.
    private static final long serialVersionUID = 1L;

    // Marks the account number as the unique identifier.
    @Id
    private final String accountNumber;
    // Specifies the maximum length of the account owner's name.
    @MaxLength(10)
    private String ownerName;
    // Ensures that the account balance must be positive.
    @Positive
    private long balance;
    private boolean active;

    // Maintains the count of generated accounts.
    private static int accountCounter = 1;

    // Generates a unique account number in a synchronized manner.
    private static synchronized String generateAccountNumber()
    {
        return String.format("AC%04d", accountCounter++);
    }

    // Sets the next account number if it is greater than the current counter.
    public static synchronized void setNextAccountNumber(int next)
    {
        if(next > accountCounter)
        {
            accountCounter = next;
        }
    }

    // Initializes a new account with owner name and initial balance.
    public Account(String ownerName, long balance)
    {
        accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        active = true;
    }

    // Adds the specified amount to the account balance.
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

    // Withdraws the specified amount from the account balance.
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

    // Transfers money from this account to another account.
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

    // Returns the available balance of the account.
    protected long availableBalance()
    {
        return balance;
    }

    // Returns the account number.
    public String getAccountNumber()
    {
        return accountNumber;
    }

    // Returns the account owner's name.
    public String getOwnerName()
    {
        return ownerName;
    }

    // Returns the current account balance.
    @Override
    public long getBalance()
    {
        return balance;
    }

    // Returns the active status of the account.
    public boolean getActive()
    {
        return active;
    }

    // Defines the interest rate for subclasses.
    @Override
    public abstract double interestRate();

    // Checks whether the specified amount can be withdrawn.
    public abstract boolean canWithdraw(long amount);

    // Compares two accounts based on their account numbers.
    @Override
    public int compareTo(Account other)
    {
        return accountNumber.compareTo(other.accountNumber);
    }

    // Returns the account details as a string.
    @Override
    public String toString()
    {
        return "Account Number : " + accountNumber +
               ", Owner Name : " + ownerName +
               ", Balance : " + balance;
    }

    // Checks whether two accounts have the same account number.
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

    // Returns the hash code based on the account number.
    @Override
    public int hashCode()
    {
        return accountNumber.hashCode();
    }
}