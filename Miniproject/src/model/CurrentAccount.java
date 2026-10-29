package model;

public class CurrentAccount extends Account
{
    private long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit)
    {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate()
    {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount)
    {
        return getBalance() - amount >= -overdraftLimit;
    }

    @Override
    protected long availableBalance()
    {
        return getBalance() + overdraftLimit;
    }

    public long getOverdraftLimit()
    {
        return overdraftLimit;
    }
}