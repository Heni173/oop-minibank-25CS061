package exception;

// Parent of all custom bank exceptions.
// It extends Exception (checked), so callers must catch it or declare it with throws.
public class BankException extends Exception
{
    public BankException(String message)
    {
        super(message);
    }
}