package model;

// Record representing a banking command with transaction details.
public record Command(TransactionType type, String accountNumber, long amount)
{
}