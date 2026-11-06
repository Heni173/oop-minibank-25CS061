package model;

// Represents a current bank account with an overdraft facility.
public class CurrentAccount extends Account
{
    // Defines the unique version identifier for serialization.
    private static final long serialVersionUID = 1L;

    // Stores the maximum overdraft limit.
    private long overdraftLimit;

    // Initializes a current account with owner name, balance and overdraft limit.
    public CurrentAccount(String ownerName, long balance, long overdraftLimit)
    {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    // Returns the interest rate for the current account.
    @Override
    public double interestRate()
    {
        return 0.0;
    }

    // Checks whether the withdrawal is within the overdraft limit.
    @Override
    public boolean canWithdraw(long amount)
    {
        return getBalance() - amount >= -overdraftLimit;
    }

    // Returns the available balance including the overdraft limit.
    @Override
    protected long availableBalance()
    {
        return getBalance() + overdraftLimit;
    }

    // Returns the overdraft limit.
    public long getOverdraftLimit()
    {
        return overdraftLimit;
    }
}