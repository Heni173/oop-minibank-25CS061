package service;

import model.Command;
import model.TransactionType;

public class TransactionProducer implements Runnable
{
    private final TransactionBuffer buffer;
    private final String accountNumber;
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
                TransactionType type =
                    (i % 3 == 0) ? TransactionType.WITHDRAW : TransactionType.DEPOSIT;

                long amount =
                    (type == TransactionType.WITHDRAW) ? 50 : 100;

                Command command =
                    new Command(type, accountNumber, amount);

                buffer.put(command);

                System.out.println(
                    "Produced #" + i + " : " + command.type() + " " + command.amount()
                );

                Thread.sleep(30);
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Producer was interrupted");
        }
    }
}