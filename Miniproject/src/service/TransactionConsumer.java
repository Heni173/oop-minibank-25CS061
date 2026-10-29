package service;

import exception.BankException;
import model.Account;
import model.Command;

public class TransactionConsumer implements Runnable
{
    private final TransactionBuffer buffer;
    private final Account account;
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
                    System.out.println("Consumer error : " + e.getMessage());
                }

                Thread.sleep(150);
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Consumer was interrupted");
        }
    }
}