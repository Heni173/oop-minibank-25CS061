package service;

import model.Command;
import model.TransactionType;

// Producer thread : creates commands and puts them into the buffer
public class TransactionProducer implements Runnable
{
    private final TransactionBuffer buffer;
    private final String accountNumber;

    // Number of commands to produce before the thread finishes
    private final int total;

    public TransactionProducer(TransactionBuffer buffer, String accountNumber, int total)
    {
        this.buffer = buffer;
        this.accountNumber = accountNumber;
        this.total = total;
    }

    @Override
    public void run()
    {
        try
        {
            for(int i = 1; i <= total; i++)
            {
                // Every 3rd command is a withdraw of 50, the others are deposits of 100
                TransactionType type =
                    (i % 3 == 0) ? TransactionType.WITHDRAW : TransactionType.DEPOSIT;

                long amount =
                    (type == TransactionType.WITHDRAW) ? 50 : 100;

                Command command =
                    new Command(type, accountNumber, amount);

                // Waits here if the buffer is full
                buffer.put(command);

                System.out.println(
                    "Produced #" + i + " : " + command.type() + " " + command.amount()
                );

                // Small delay between commands
                Thread.sleep(30);
            }
        }
        catch(InterruptedException e)
        {
            // Thrown by put() or sleep() if the thread is interrupted
            System.out.println("Producer was interrupted");
        }
    }
}