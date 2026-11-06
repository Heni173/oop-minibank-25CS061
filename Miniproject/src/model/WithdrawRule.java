package model;

// Functional interface defining a rule for withdrawal operations.
@FunctionalInterface
public interface WithdrawRule
{
    // Checks whether the specified withdrawal is allowed.
    boolean allow(Account account, long amount);
}