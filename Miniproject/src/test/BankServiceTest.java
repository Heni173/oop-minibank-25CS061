package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import exception.BankException;
import exception.InsufficientFundsException;
import model.Account;
import service.BankService;

// Test class for verifying BankService operations.
public class BankServiceTest
{
    // Creates a new BankService instance using temporary files.
    private BankService newService() throws IOException
    {
        Path folder = Files.createTempDirectory("minibank-test");

        return new BankService(
            folder.resolve("bank.dat"),
            folder.resolve("logs"),
            folder.resolve("report.txt")
        );
    }

    // Tests whether depositing money increases the account balance.
    @Test
    public void depositIncreasesBalance() throws Exception
    {
        BankService service = newService();

        Account account =
            service.openAccount("SAVINGS", "Tester", "9876543210", 5000);

        service.deposit(account.getAccountNumber(), 700);

        assertEquals(5700L, account.getBalance());
    }

    // Tests whether withdrawing beyond the allowed limit throws an exception.
    @Test
    public void withdrawalOverTheLimitThrowsException() throws Exception
    {
        BankService service = newService();

        Account account =
            service.openAccount("SAVINGS", "Tester", "9876543210", 5000);

        InsufficientFundsException error =
            assertThrows(
                InsufficientFundsException.class,
                () -> service.withdraw(account.getAccountNumber(), 4500)
            );

        assertEquals(500L, error.getShortfall());
        assertEquals(5000L, account.getBalance());
    }

    // Tests whether concurrent deposits maintain the correct account balance.
    @Test
    public void manyConcurrentDepositsKeepBalanceCorrect() throws Exception
    {
        BankService service = newService();

        Account account =
            service.openAccount("CURRENT", "Tester", "9876543210", 1000);

        String number = account.getAccountNumber();

        int threadCount = 10;
        int depositsPerThread = 100;

        Thread[] threads = new Thread[threadCount];

        // Creates multiple threads to perform concurrent deposits.
        for(int i = 0; i < threadCount; i++)
        {
            threads[i] = new Thread(() ->
            {
                for(int j = 0; j < depositsPerThread; j++)
                {
                    try
                    {
                        service.deposit(number, 1);
                    }
                    catch(BankException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            });

            threads[i].start();
        }

        // Waits for all threads to complete their operations.
        for(int i = 0; i < threadCount; i++)
        {
            threads[i].join();
        }

        assertEquals(1000L + threadCount * depositsPerThread, account.getBalance());
    }

    // Tests whether money is transferred correctly between two accounts.
    @Test
    public void transferMovesMoneyBetweenAccounts() throws Exception
    {
        BankService service = newService();

        Account from =
            service.openAccount("SAVINGS", "Sender", "9876543210", 5000);

        Account to =
            service.openAccount("SAVINGS", "Receiver", "9123456780", 2000);

        service.transfer(from.getAccountNumber(), to.getAccountNumber(), 1500);

        assertEquals(3500L, from.getBalance());
        assertEquals(3500L, to.getBalance());
    }

    // Tests whether an invalid mobile number is rejected.
    @Test
    public void invalidMobileNumberIsRejected() throws Exception
    {
        BankService service = newService();

        assertThrows(
            BankException.class,
            () -> service.openAccount("SAVINGS", "Tester", "1234567890", 5000)
        );
    }
}