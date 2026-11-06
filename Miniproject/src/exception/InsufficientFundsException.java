package exception;

// Thrown when a withdrawal or transfer amount is more than the account can give
public class InsufficientFundsException extends BankException
{
    // How much money is missing (requested amount minus available amount)
    private final long shortfall;

    public InsufficientFundsException(String message, long shortfall)
    {
        super(message);
        this.shortfall = shortfall;
    }

    public long getShortfall()
    {
        return shortfall;
    }
}