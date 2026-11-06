package service;

import exception.BankException;
import model.Account;
import model.Command;

// Consumer thread : takes commands from the buffer and applies them to the account
public class TransactionConsumer implements Runnable
{
    private final TransactionBuffer buffer;
    private final Account account;

    // Number of commands to consume before the thread finishes
    private final int total;

    public TransactionConsumer(TransactionBuffer buffer, Account account, int total)
    {
        this.buffer = buffer;
        this.account = account;
        this.total = total;
    }

    @Override
    public void run()
    {
        try
        {
            for(int i = 1; i <= total; i++)
            {
                // Waits here if the buffer is empty
                Command command = buffer.take();

                try
                {
                    switch(command.type())
                    {
                        case DEPOSIT:
                            account.deposit(command.amount());
                            break;

                        case WITHDRAW:
                            account.withdraw(command.amount());
                            break;

                        default:
                            System.out.println("Unsupported command");
                    }

                    System.out.println(
                        "Consumed #" + i + " : " + command.type() + " " +
                        command.amount() + ", balance now " + account.getBalance()
                    );
                }
                catch(BankException e)
                {
                    // A failed transaction is reported and the consumer continues with the next one
                    System.out.println("Consumer error : " + e.getMessage());
                }

                // Small delay so the producer and consumer speeds differ (shows waiting in the output)
                Thread.sleep(150);
            }
        }
        catch(InterruptedException e)
        {
            // Thrown by take() or sleep() if the thread is interrupted
            System.out.println("Consumer was interrupted");
        }
    }
}