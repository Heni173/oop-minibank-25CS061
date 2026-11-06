package util;

// Logger class for recording transaction messages.
public class TransactionLogger implements AutoCloseable
{
    private final String name;

    // Initializes the logger with the specified name.
    public TransactionLogger(String name)
    {
        this.name = name;
        System.out.println("Logger '" + name + "' opened");
    }

    // Logs the given transaction message.
    public void log(String message)
    {
        System.out.println("[" + name + "] " + message);
    }

    // Closes the logger and displays a closing message.
    @Override
    public void close()
    {
        System.out.println("Logger '" + name + "' closed");
    }
}