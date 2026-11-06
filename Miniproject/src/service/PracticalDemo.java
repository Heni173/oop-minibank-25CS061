package service;

import static util.Validator.isValidMobile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

public class PracticalDemo
{
    // Record: immutable holder for bank name and branch
    record BankInfo(String name, String branch)
    {
    }

    // Linear search by account number, throws if no account matches
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

        // Nested static class used for the customer's address
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

        // One array holds all account types (polymorphism)
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

        // Pattern matching instanceof: s is already cast to SavingsAccount
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

        // Premium has no methods, it only tags the account types that implement it
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

        // Parses the text command into a Command record (type, account number, amount)
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

        // Anonymous class: one-time implementation of the WithdrawRule interface
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

        // Same interface as above, written as a lambda (works because it has one abstract method)
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

        // Validator reads the custom annotations on the account fields using reflection
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

        // finally runs whether the withdrawal succeeds or fails
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

        // BankException is the parent of all custom bank exceptions, so one catch handles them all
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

        // The logger is closed automatically, even when an exception is thrown inside the block
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

        // Every worker deposits workerAmount, workerTimes times
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

        // join() makes the main thread wait until every worker finishes
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

        // A mismatch means threads overwrote each other's updates (race condition)
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

        // 10 deposits of 100 and 10 withdrawals of 50 : 10000 + 1000 - 500 = 10500
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

        // stop() waits for the submitted tasks to finish before the balance is read
        processor.stop();

        System.out.println(
            "All tasks finished. Final balance : " +
            poolAccount.getBalance() +
            " (expected 10500)"
        );

        System.out.println("\n----- Producer-Consumer : wait / notify -----");

        Account bufferAccount =
            new SavingsAccount("BUFFER", 1000, 0);

        // Buffer holds at most 3 items, so the producer waits when it is full
        // and the consumer waits when it is empty
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

        // false = locks taken in opposite order, this can deadlock
        TransferDemo.runDemo(false);

        System.out.println("\n----- Deadlock fixed : smaller account number first -----");

        // true = both threads lock in the same order, so no deadlock
        TransferDemo.runDemo(true);

        System.out.println("\n----- Persistence : save and load accounts -----");

        Path dataFile = Paths.get("data", "accounts.dat");

        StatePersister.save(acc, dataFile);

        System.out.println(
            "Saved " + acc.length + " accounts to " + dataFile
        );

        Account[] loaded = StatePersister.load(dataFile);

        boolean survived = loaded.length == acc.length;

        for(int i = 0; i < loaded.length; i++)
        {
            System.out.println("Original : " + acc[i]);
            System.out.println("Loaded   : " + loaded[i]);

            if(!loaded[i].equals(acc[i])
               || loaded[i].getBalance() != acc[i].getBalance()
               || !loaded[i].getOwnerName().equals(acc[i].getOwnerName()))
            {
                survived = false;
            }
        }

        System.out.println("Data survived : " + survived);
        System.out.println(
            "Same object in memory : " + (loaded[0] == acc[0]) +
            " (loaded accounts are fresh copies)"
        );

        System.out.println("\n----- Transaction Log : append -----");

        Path logsFolder = Paths.get("logs");
        Path morningLog = logsFolder.resolve("morning.log");
        Path eveningLog = logsFolder.resolve("evening.log");

        // Clear old content so every run starts with fresh log files
        TransactionLog.clear(morningLog);
        TransactionLog.clear(eveningLog);

        TransactionLog.append(morningLog, TransactionLog.line("DEPOSIT", "AC0001", 500));
        TransactionLog.append(morningLog, TransactionLog.line("DEPOSIT", "AC0002", 1200));
        TransactionLog.append(morningLog, TransactionLog.line("WITHDRAW", "AC0001", 200));

        TransactionLog.append(eveningLog, TransactionLog.line("WITHDRAW", "AC0002", 300));
        TransactionLog.append(eveningLog, TransactionLog.line("DEPOSIT", "AC0003", 2000));
        TransactionLog.append(eveningLog, TransactionLog.line("WITHDRAW", "AC0001", 100));

        System.out.println("Log entries written to " + logsFolder);

        System.out.println("\n----- End of Day Report -----");

        Path reportFile = Paths.get("reports", "end-of-day.txt");

        // Reads every log file in the folder and builds one summary report
        List<String> report = ReportGenerator.generate(logsFolder, reportFile);

        for(String reportLine : report)
        {
            System.out.println(reportLine);
        }

        System.out.println("Report saved to " + reportFile);

        System.out.println("\n----- Collections : AccountStore -----");

        AccountStore store = new AccountStore();

        Account dev = new SavingsAccount("DEV", 7500, 500);
        Account meera = new CurrentAccount("MEERA", 12000, 3000);
        Account arjun = new FixedDepositAccount("ARJUN", 2500);

        store.add(acc[0]);
        store.add(acc[1]);
        store.add(acc[2]);
        store.add(dev);
        store.add(meera);
        store.add(arjun);

        System.out.println("Accounts in store : " + store.size());

        try
        {
            System.out.println("Lookup " + dev.getAccountNumber() + " : " + store.get(dev.getAccountNumber()));
            System.out.println("Lookup AC9999 : " + store.get("AC9999"));
        }
        catch(AccountNotFoundException e)
        {
            System.out.println("Lookup failed : " + e.getMessage());
        }

        // The fixed deposit withdrawal is expected to fail and be caught below
        try
        {
            store.deposit(dev.getAccountNumber(), 500);
            store.withdraw(meera.getAccountNumber(), 2000);
            store.withdraw(arjun.getAccountNumber(), 100);
        }
        catch(BankException e)
        {
            System.out.println("Store transaction failed : " + e.getMessage());
        }

        System.out.println("\nTransaction history (ArrayList) :");

        List<String> historyList = store.getHistory();

        for(int i = 0; i < historyList.size(); i++)
        {
            System.out.println((i + 1) + ". " + historyList.get(i));
        }

        System.out.println("\nSorted by balance (highest first, tie : account number) :");

        ArrayList<Account> sortedList = store.sortedByBalance();

        for(int i = 0; i < sortedList.size(); i++)
        {
            System.out.println((i + 1) + ". " + sortedList.get(i));
        }

        System.out.println("\nSorted by account number (Comparable) :");

        List<Account> byNumber = store.getAll();

        Collections.sort(byNumber);

        for(int i = 0; i < byNumber.size(); i++)
        {
            System.out.println((i + 1) + ". " + byNumber.get(i));
        }

        System.out.println("\n----- ConcurrentHashMap in TransactionProcessor -----");

        TransactionProcessor engine = new TransactionProcessor();

        List<Account> storeAccounts = store.getAll();

        Collections.sort(storeAccounts);
        long[] balanceBefore = new long[storeAccounts.size()];

        for(int i = 0; i < storeAccounts.size(); i++)
        {
            engine.register(storeAccounts.get(i));
            balanceBefore[i] = storeAccounts.get(i).getBalance();
        }

        // 50 extra accounts are registered by pool threads at the same time
        for(int i = 1; i <= 50; i++)
        {
            Account extra = new SavingsAccount("EXTRA" + i, 100, 0);

            engine.submit(() -> engine.register(extra));
        }

        // 36 deposits of 10 spread over 6 accounts = 6 deposits each = +60 per account
        for(int i = 0; i < 36; i++)
        {
            String number =
                storeAccounts.get(i % storeAccounts.size()).getAccountNumber();

            engine.submitTransaction(number, TransactionType.DEPOSIT, 10);
        }

        engine.stop();

        boolean allCorrect = true;

        for(int i = 0; i < storeAccounts.size(); i++)
        {
            long expectedNow = balanceBefore[i] + 60;
            long actualNow = storeAccounts.get(i).getBalance();

            System.out.println(
                storeAccounts.get(i).getAccountNumber() +
                " : " + balanceBefore[i] + " -> " + actualNow +
                " (expected " + expectedNow + ")"
            );

            if(expectedNow != actualNow)
            {
                allCorrect = false;
            }
        }

        System.out.println("All balances correct : " + allCorrect);
        // 6 store accounts + 50 extra accounts = 56
        System.out.println(
            "Accounts in concurrent map : " + engine.accountCount() +
            " (expected 56)"
        );

        System.out.println("\n----- Generics : Repository -----");

        // One generic Repository class works for any type that has an @Id field
        Repository<Account> accountRepository = new Repository<>();

        accountRepository.save(acc[0]);
        accountRepository.saveAll(acc[1], acc[2], dev, meera, arjun);

        System.out.println(
            "Accounts saved in repository : " + accountRepository.size()
        );

        Account foundAccount =
            accountRepository.findById(dev.getAccountNumber());

        System.out.println(
            "findById(" + dev.getAccountNumber() + ") : " + foundAccount
        );

        Account notFound = accountRepository.findById("AC9999");

        System.out.println("findById(AC9999) : " + notFound);

        System.out.println(
            "delete(" + arjun.getAccountNumber() + ") : " +
            accountRepository.delete(arjun.getAccountNumber())
        );

        System.out.println(
            "delete(" + arjun.getAccountNumber() + ") again : " +
            accountRepository.delete(arjun.getAccountNumber())
        );

        System.out.println("\nAll accounts in repository :");

        ArrayList<Account> allAccounts = accountRepository.findAll();

        Collections.sort(allAccounts);

        for(int i = 0; i < allAccounts.size(); i++)
        {
            System.out.println((i + 1) + ". " + allAccounts.get(i));
        }

        System.out.println("\n----- Generics : same Repository for Customer -----");

        Repository<Customer> customerRepository = new Repository<>();

        Customer neha =
            new Customer("Neha", "neha@gmail.com", "9876543210", address);

        Customer rahul =
            new Customer("Rahul", "rahul@gmail.com", "9123456780", address);

        customerRepository.save(c1);
        customerRepository.saveAll(neha, rahul);

        System.out.println(
            "Customers saved in repository : " + customerRepository.size()
        );

        Customer foundCustomer =
            customerRepository.findById(neha.getCustomerId());

        System.out.println(
            "findById(" + neha.getCustomerId() + ") : " +
            foundCustomer.getName() + ", " + foundCustomer.getEmail()
        );

        System.out.println("\n----- Generics : object without @Id -----");

        Repository<String> textRepository = new Repository<>();

        // String has no @Id field, so save() is expected to throw
        try
        {
            textRepository.save("hello");
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Save failed : " + e.getMessage());
        }

        System.out.println("\n----- Generics : bounded type parameter -----");

        Long[] balances = new Long[allAccounts.size()];

        for(int i = 0; i < allAccounts.size(); i++)
        {
            balances[i] = allAccounts.get(i).getBalance();
        }

        Integer[] marks = {80, 90, 70, 100};
        Double[] rates = {4.0, 0.0, 7.0};

        // average() accepts any array of Number subclasses (Long, Integer, Double)
        System.out.println("Average balance (Long[])  : " + Repository.average(balances));
        System.out.println("Average marks (Integer[]) : " + Repository.average(marks));
        System.out.println("Average rate (Double[])   : " + Repository.average(rates));

        // Menu loop runs until the user chooses Exit
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

                    // Tries the same amount on every account and reports each result separately
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