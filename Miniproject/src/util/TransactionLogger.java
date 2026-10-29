package util;

public class TransactionLogger implements AutoCloseable
{
    private final String name;

    public TransactionLogger(String name)
    {
        this.name = name;
        System.out.println("Logger '" + name + "' opened");
    }

    public void log(String message)
    {
        System.out.println("[" + name + "] " + message);
    }

    @Override
    public void close()
    {
        System.out.println("Logger '" + name + "' closed");
    }
}