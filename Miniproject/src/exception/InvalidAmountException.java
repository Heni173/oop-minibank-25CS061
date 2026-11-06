package exception;

// Thrown when an amount is not valid, e.g. zero or negative
public class InvalidAmountException extends BankException
{
    public InvalidAmountException(String message)
    {
        super(message);
    }
}