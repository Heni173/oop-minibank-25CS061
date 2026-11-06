package exception;

// Thrown when no account matches the given account number
public class AccountNotFoundException extends BankException
{
    public AccountNotFoundException(String accountNumber)
    {
        // Passes the message to BankException, getMessage() will return it
        super("Account not found : " + accountNumber);
    }
}