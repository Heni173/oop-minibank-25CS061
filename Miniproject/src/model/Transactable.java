package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

// Interface defining deposit and withdrawal operations.
public interface Transactable
{
    // Defines the deposit operation.
    void deposit(long amount) throws InvalidAmountException;

    // Defines the withdrawal operation.
    void withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}