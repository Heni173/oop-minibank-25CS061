package model;

// Interface for accounts that provide interest.
public interface InterestBearing
{
    // Returns the applicable interest rate.
    double interestRate();

    // Returns the current account balance.
    long getBalance();

    // Calculates the yearly interest based on balance and interest rate.
    default double yearlyInterest()
    {
        return getBalance() * interestRate() / 100;
    }
}