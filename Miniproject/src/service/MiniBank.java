package service;

import static util.Validator.isValidMobile;

import java.util.Scanner;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;
import model.TransactionType;
import model.WithdrawRule;
import util.AnnotationValidator;
import util.CommandParser;
import util.StatementFormatter;
import util.TransactionLogger;
import util.Validator;

public class MiniBank
{
    record BankInfo(String name, String branch)
    {
    }

    static Account findAccount(Account[] accounts, String number) throws AccountNotFoundException
    {
        for(int i = 0; i < accounts.length; i++)
        {
            if(accounts[i].getAccountNumber().equals(number))
            {
                return accounts[i];
            }
        }

        throw new AccountNotFoundException(number);
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        BankInfo bank =
            new BankInfo("MiniBank", "Main Branch");

        Customer.Address address =
            new Customer.Address(
                "123 Main Road",
                "Vadodara",
                "390001"
            );

        Customer c1 =
            new Customer(
                "ABC",
                "ABC@gmail.com",
                "7984561230",
                address
            );

        Account[] acc = new Account[3];

        acc[0] =
            new SavingsAccount("ABC", 5000, 1000);

        acc[1] =
            new CurrentAccount("XYZ", 3000, 2000);

        acc[2] =
            new FixedDepositAccount("PQR", 10000);

        System.out.println("----- Account Details -----");

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(acc[i]);
        }

        System.out.println("\n----- Interest Rate -----");

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(
                acc[i].getAccountNumber() +
                " : " +
                acc[i].interestRate() +
                "%"
            );
        }

        System.out.println("\n----- Yearly Interest (default method) -----");

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(
                acc[i].getAccountNumber() +
                " : " +
                acc[i].yearlyInterest()
            );
        }

        System.out.println("\n----- instanceof -----");

        for(int i = 0; i < acc.length; i++)
        {
            if(acc[i] instanceof SavingsAccount s)
            {
                System.out.println(
                    s.getAccountNumber() +
                    " is a SavingsAccount, Min Balance : " +
                    s.getMinBalance()
                );
            }
        }

        System.out.println("\n----- Premium (marker interface) -----");

        for(int i = 0; i < acc.length; i++)
        {
            if(acc[i] instanceof Premium)
            {
                System.out.println(
                    acc[i].getAccountNumber() +
                    " is a Premium account"
                );
            }
        }

        System.out.println("\n----- equals() -----");

        Account testAccount = acc[0];

        if(acc[0].equals(testAccount))
        {
            System.out.println(
                "Both Account objects are equal"
            );
        }
        else
        {
            System.out.println(
                "Both Account objects are not equal"
            );
        }

        System.out.println("\n----- Customer Address -----");

        System.out.println(
            "Line : " +
            c1.getAddress().getLine()
        );

        System.out.println(
            "City : " +
            c1.getAddress().getCity()
        );

        System.out.println(
            "Pincode : " +
            c1.getAddress().getPincode()
        );

        Customer c2 = c1.clone();

        System.out.println("\n----- Clone -----");

        System.out.println(
            "Original : " + c1.getName()
        );

        System.out.println(
            "Clone : " + c2.getName()
        );

        System.out.println("\n----- Validator Testing -----");

        System.out.println(
            "Mobile Correct : " +
            isValidMobile("7984561230")
        );

        System.out.println(
            "Mobile Wrong   : " +
            isValidMobile("1234567890")
        );

        System.out.println(
            "Email Correct  : " +
            Validator.isValidEmail("abc@gmail.com")
        );

        System.out.println(
            "Email Wrong    : " +
            Validator.isValidEmail("abc@gmail")
        );

        System.out.println(
            "PAN Correct    : " +
            Validator.isValidPan("ABCDE1234F")
        );

        System.out.println(
            "PAN Wrong      : " +
            Validator.isValidPan("ABC12345")
        );

        System.out.println(
            "IFSC Correct   : " +
            Validator.isValidIfsc("SBIN0001234")
        );

        System.out.println(
            "IFSC Wrong     : " +
            Validator.isValidIfsc("SBI1234")
        );

        System.out.println("\n----- Command Parser -----");

        Command command =
            CommandParser.parse(
                "DEPOSIT AC0001 500"
            );

        System.out.println(
            "Transaction Type : " +
            command.type()
        );

        System.out.println(
            "Account Number   : " +
            command.accountNumber()
        );

        System.out.println(
            "Amount           : " +
            command.amount()
        );

        System.out.println(
            StatementFormatter.buildStatement(acc[0])
        );

        System.out.println("\n----- WithdrawRule (anonymous class) -----");

        WithdrawRule limitRule = new WithdrawRule()
        {
            @Override
            public boolean allow(Account account, long amount)
            {
                return amount <= 2000;
            }
        };

        if(limitRule.allow(acc[0], 1500))
        {
            System.out.println(
                "Anonymous rule : 1500 allowed (limit is 2000)"
            );
        }

        if(!limitRule.allow(acc[0], 3000))
        {
            System.out.println(
                "Anonymous rule : 3000 blocked (limit is 2000)"
            );
        }

        System.out.println("\n----- WithdrawRule (lambda) -----");

        WithdrawRule activeRule =
            (account, amount) -> account.getActive() && account.canWithdraw(amount);

        for(int i = 0; i < acc.length; i++)
        {
            System.out.println(
                acc[i].getAccountNumber() +
                " can withdraw 500 : " +
                activeRule.allow(acc[i], 500)
            );
        }

        System.out.println("\n----- Annotation Validation -----");

        Account badAccount =
            new SavingsAccount("TEST", -500, 0);

        String[] errors =
            AnnotationValidator.validate(badAccount);

        for(int i = 0; i < errors.length; i++)
        {
            System.out.println("Error : " + errors[i]);
        }

        Account longName =
            new CurrentAccount("VeryLongOwnerName", 1000, 0);

        String[] errors2 =
            AnnotationValidator.validate(longName);

        for(int i = 0; i < errors2.length; i++)
        {
            System.out.println("Error : " + errors2[i]);
        }

        String[] okErrors =
            AnnotationValidator.validate(acc[0]);

        System.out.println(
            "Valid account errors : " + okErrors.length
        );

        System.out.println("\n----- Exception Handling : withdraw -----");

        try
        {
            acc[0].withdraw(3000);
            System.out.println("Savings Withdrawal Successful");
        }
        catch(InsufficientFundsException e)
        {
            System.out.println(
                "Savings Withdrawal Failed : " + e.getMessage()
            );
        }
        catch(InvalidAmountException e)
        {
            System.out.println(
                "Savings Withdrawal Failed : " + e.getMessage()
            );
        }
        finally
        {
            System.out.println("Savings withdrawal check finished");
        }

        try
        {
            acc[1].withdraw(4000);
            System.out.println("Current Withdrawal Successful");
        }
        catch(InsufficientFundsException e)
        {
            System.out.println(
                "Current Withdrawal Failed : " + e.getMessage()
            );
        }
        catch(InvalidAmountException e)
        {
            System.out.println(
                "Current Withdrawal Failed : " + e.getMessage()
            );
        }
        finally
        {
            System.out.println("Current withdrawal check finished");
        }

        try
        {
            acc[2].withdraw(1000);
            System.out.println("Fixed Deposit Withdrawal Successful");
        }
        catch(InsufficientFundsException e)
        {
            System.out.println(
                "Fixed Deposit Withdrawal Failed : " + e.getMessage()
            );
            System.out.println(
                "Shortfall : " + e.getShortfall()
            );
        }
        catch(InvalidAmountException e)
        {
            System.out.println(
                "Fixed Deposit Withdrawal Failed : " + e.getMessage()
            );
        }
        finally
        {
            System.out.println("Fixed deposit withdrawal check finished");
        }

        System.out.println("\n----- Exception Handling : invalid amount -----");

        try
        {
            acc[1].withdraw(-100);
        }
        catch(InsufficientFundsException e)
        {
            System.out.println("Not enough money : " + e.getMessage());
        }
        catch(InvalidAmountException e)
        {
            System.out.println("Invalid amount : " + e.getMessage());
        }

        try
        {
            acc[1].deposit(0);
        }
        catch(InvalidAmountException e)
        {
            System.out.println("Invalid amount : " + e.getMessage());
        }

        System.out.println("\n----- Exception Handling : transfer -----");

        try
        {
            acc[1].transfer(acc[0], 500);
        }
        catch(BankException e)
        {
            System.out.println("Transfer error : " + e.getMessage());
        }

        try
        {
            acc[2].transfer(acc[0], 100);
        }
        catch(BankException e)
        {
            System.out.println("Transfer error : " + e.getMessage());
        }

        try
        {
            Account missing = findAccount(acc, "AC9999");
            acc[0].transfer(missing, 100);
        }
        catch(BankException e)
        {
            System.out.println("Transfer error : " + e.getMessage());
        }

        System.out.println("\n----- try-with-resources -----");

        try(TransactionLogger logger = new TransactionLogger("bank-log"))
        {
            logger.log("Depositing 500 in " + acc[0].getAccountNumber());
            acc[0].deposit(500);
            logger.log("Deposit done, balance : " + acc[0].getBalance());

            logger.log("Trying an invalid deposit");
            acc[0].deposit(-1);

            logger.log("This line is never printed");
        }
        catch(InvalidAmountException e)
        {
            System.out.println("Caught after logger closed : " + e.getMessage());
        }

        System.out.println("\n----- Multithreading : shared account -----");

        int threadCount = 10;
        int workerTimes = 1000000;
        long workerAmount = 1;

        Account sharedAccount =
            new SavingsAccount("RACE", 1000, 0);

        long startBalance = sharedAccount.getBalance();

        long expectedBalance =
            startBalance + (long) threadCount * workerTimes * workerAmount;

        System.out.println(
            "Main thread name : " + Thread.currentThread().getName()
        );

        Thread[] threads = new Thread[threadCount];

        for(int i = 0; i < threadCount; i++)
        {
            threads[i] =
                new Thread(
                    new AccountWorker(sharedAccount, workerTimes, workerAmount),
                    "Worker-" + (i + 1)
                );

            System.out.println(
                threads[i].getName() +
                " state before start : " +
                threads[i].getState()
            );
        }

        for(int i = 0; i < threadCount; i++)
        {
            threads[i].start();
        }

        for(int i = 0; i < threadCount; i++)
        {
            System.out.println(
                threads[i].getName() +
                " state after start : " +
                threads[i].getState()
            );
        }

        try
        {
            for(int i = 0; i < threadCount; i++)
            {
                threads[i].join();
            }
        }
        catch(InterruptedException e)
        {
            System.out.println("Main thread was interrupted");
        }

        for(int i = 0; i < threadCount; i++)
        {
            System.out.println(
                threads[i].getName() +
                " state after join : " +
                threads[i].getState()
            );
        }

        long actualBalance = sharedAccount.getBalance();

        System.out.println("Start balance    : " + startBalance);
        System.out.println("Expected balance : " + expectedBalance);
        System.out.println("Actual balance   : " + actualBalance);

        if(actualBalance == expectedBalance)
        {
            System.out.println("Result : CORRECT, no money lost");
        }
        else
        {
            System.out.println(
                "Result : WRONG, lost " +
                (expectedBalance - actualBalance) +
                " (race condition)"
            );
        }

        System.out.println("\n----- Thread Pool : TransactionProcessor -----");

        Account poolAccount =
            new SavingsAccount("POOL", 10000, 0);

        TransactionProcessor processor =
            new TransactionProcessor();

        for(int i = 1; i <= 20; i++)
        {
            if(i % 2 == 0)
            {
                processor.submit(
                    new TransactionTask(i, poolAccount, TransactionType.WITHDRAW, 50)
                );
            }
            else
            {
                processor.submit(
                    new TransactionTask(i, poolAccount, TransactionType.DEPOSIT, 100)
                );
            }
        }

        processor.stop();

        System.out.println(
            "All tasks finished. Final balance : " +
            poolAccount.getBalance() +
            " (expected 10500)"
        );

        System.out.println("\n----- Producer-Consumer : wait / notify -----");

        Account bufferAccount =
            new SavingsAccount("BUFFER", 1000, 0);

        TransactionBuffer buffer =
            new TransactionBuffer(3);

        Thread producer =
            new Thread(
                new TransactionProducer(buffer, bufferAccount.getAccountNumber(), 8),
                "Producer"
            );

        Thread consumer =
            new Thread(
                new TransactionConsumer(buffer, bufferAccount, 8),
                "Consumer"
            );

        producer.start();
        consumer.start();

        try
        {
            producer.join();
            consumer.join();
        }
        catch(InterruptedException e)
        {
            System.out.println("Main thread was interrupted");
        }

        System.out.println(
            "Final balance : " +
            bufferAccount.getBalance() +
            " (expected 1500)"
        );

        System.out.println("\n----- Deadlock : opposite lock order -----");

        TransferDemo.runDemo(false);

        System.out.println("\n----- Deadlock fixed : smaller account number first -----");

        TransferDemo.runDemo(true);

        while(true)
        {
            System.out.println(
                "\n--------------------" +
                bank.name() +
                "--------------------"
            );

            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice : ");

            int choice = sc.nextInt();

            switch(choice)
            {
                case 1:

                    System.out.println(
                        "\nCustomer ID : " +
                        c1.getCustomerId()
                    );

                    System.out.println(
                        "Customer Name : " +
                        c1.getName()
                    );

                    for(int i = 0; i < acc.length; i++)
                    {
                        System.out.println(acc[i]);
                    }

                    break;

                case 2:

                    try
                    {
                        acc[0].deposit(1000);
                        acc[1].deposit(500);
                        acc[2].deposit(2000);

                        System.out.println(
                            "Deposit Successful"
                        );
                    }
                    catch(InvalidAmountException e)
                    {
                        System.out.println(
                            "Deposit Failed : " + e.getMessage()
                        );
                    }

                    for(int i = 0; i < acc.length; i++)
                    {
                        System.out.println(
                            acc[i].getAccountNumber() +
                            " Balance : " +
                            acc[i].getBalance()
                        );
                    }

                    break;

                case 3:

                    System.out.print(
                        "Enter withdrawal amount : "
                    );

                    long amount = sc.nextLong();

                    for(int i = 0; i < acc.length; i++)
                    {
                        try
                        {
                            acc[i].withdraw(amount);

                            System.out.println(
                                acc[i].getAccountNumber() +
                                " Withdrawal Successful"
                            );
                        }
                        catch(InsufficientFundsException e)
                        {
                            System.out.println(
                                acc[i].getAccountNumber() +
                                " Withdrawal Failed, short by " +
                                e.getShortfall()
                            );
                        }
                        catch(InvalidAmountException e)
                        {
                            System.out.println(
                                acc[i].getAccountNumber() +
                                " Withdrawal Failed : " +
                                e.getMessage()
                            );
                        }
                    }

                    break;

                case 4:

                    System.out.print("From account number : ");
                    String fromNo = sc.next();

                    System.out.print("To account number : ");
                    String toNo = sc.next();

                    System.out.print("Amount : ");
                    long transferAmount = sc.nextLong();

                    try
                    {
                        Account from = findAccount(acc, fromNo);
                        Account to = findAccount(acc, toNo);

                        from.transfer(to, transferAmount);
                    }
                    catch(BankException e)
                    {
                        System.out.println(
                            "Transfer not done : " + e.getMessage()
                        );
                    }

                    break;

                case 5:

                    System.out.println(
                        "Successfully exited from your account"
                    );

                    sc.close();
                    return;

                default:

                    System.out.println(
                        "Invalid choice!! Please try again."
                    );
            }
        }
    }
}