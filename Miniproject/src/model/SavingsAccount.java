package model;

// Represents a savings account with a minimum balance requirement.
public class SavingsAccount extends Account
{
    // Defines the unique version identifier for serialization.
    private static final long serialVersionUID = 1L;

    // Stores the minimum balance required in the account.
    private long minBalance;

    // Initializes a savings account with owner name, balance and minimum balance.
    public SavingsAccount(String ownerName, long balance, long minBalance)
    {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    // Returns the interest rate for the savings account.
    @Override
    public double interestRate()
    {
        return 4.0;
    }

    // Checks whether the withdrawal maintains the minimum balance.
    @Override
    public boolean canWithdraw(long amount)
    {
        return getBalance() - amount >= minBalance;
    }

    // Returns the available balance after maintaining the minimum balance.
    @Override
    protected long availableBalance()
    {
        return getBalance() - minBalance;
    }

    // Returns the minimum balance requirement.
    public long getMinBalance()
    {
        return minBalance;
    }
}