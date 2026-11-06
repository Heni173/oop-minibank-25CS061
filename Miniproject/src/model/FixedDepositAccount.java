package model;

// Represents a fixed deposit account with a fixed interest rate.
public class FixedDepositAccount extends Account implements Premium
{
    // Defines the unique version identifier for serialization.
    private static final long serialVersionUID = 1L;

    // Initializes a fixed deposit account with owner name and balance.
    public FixedDepositAccount(String ownerName, long balance)
    {
        super(ownerName, balance);
    }

    // Returns the interest rate for the fixed deposit account.
    @Override
    public double interestRate()
    {
        return 7.0;
    }

    // Prevents withdrawals from the fixed deposit account.
    @Override
    public boolean canWithdraw(long amount)
    {
        return false;
    }

    // Returns zero as the available withdrawal balance.
    @Override
    protected long availableBalance()
    {
        return 0;
    }
}