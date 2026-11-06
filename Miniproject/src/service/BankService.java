package service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import model.annotation.Audit;
import util.StatementFormatter;
import util.Validator;

// BankService implements all main banking operations of the MiniBank system.
public class BankService implements BankOperations
{
    // Minimum balance required for opening a savings account.
    private static final long SAVINGS_MIN_BALANCE = 1000;

    // Maximum overdraft limit allowed for a current account.
    private static final long CURRENT_OVERDRAFT = 2000;

    // Generic repository used to store and manage bank accounts.
    private final Repository<Account> repository = new Repository<>();

    // Lock object used to synchronize transaction log operations.
    private final Object logLock = new Object();

    // Stores the path of the account data file.
    private final Path dataFile;

    // Stores the folder path for transaction logs.
    private final Path logsFolder;

    // Stores the path of the generated report file.
    private final Path reportFile;

    // Constructor to initialize file and folder paths.
    public BankService(Path dataFile, Path logsFolder, Path reportFile)
    {
        this.dataFile = dataFile;
        this.logsFolder = logsFolder;
        this.reportFile = reportFile;
    }

    // Finds an account using its account number.
    // Throws AccountNotFoundException if the account does not exist.
    private Account find(String accountNumber) throws AccountNotFoundException
    {
        // Removes extra spaces and converts the account number to uppercase.
        String number =
            (accountNumber == null) ? "" : accountNumber.trim().toUpperCase();

        // Searches for the account in the repository.
        Account account = repository.findById(number);

        if(account == null)
        {
            throw new AccountNotFoundException(number);
        }

        return account;
    }

    // Creates a transaction log entry and appends it to the daily log file.
    private void log(String type, String accountNumber, long amount)
    {
        // Creates a daily transaction log file using the current date.
        Path file = logsFolder.resolve("transactions-" + LocalDate.now() + ".log");

        // Ensures that log writing is performed by only one thread at a time.
        synchronized(logLock)
        {
            TransactionLog.append(file, TransactionLog.line(type, accountNumber, amount));
        }
    }

    // Calculates and returns the total balance of all accounts.
    private long totalBalance()
    {
        long total = 0;

        // Adds the balance of each account to the total.
        for(Account account : repository.findAll())
        {
            total = total + account.getBalance();
        }

        return total;
    }

    // Opens a new account after validating the provided details.
    @Override
    @Audit("Open a new account")
    public synchronized Account openAccount(String type, String ownerName, String mobile, long openingBalance) throws BankException
    {
        // Checks whether the account type is provided.
        if(type == null)
        {
            throw new BankException("Account type is required");
        }

        // Checks whether the owner's name is empty or null.
        if(ownerName == null || ownerName.isBlank())
        {
            throw new BankException("Owner name must not be empty");
        }

        // Checks whether the owner's name exceeds 10 characters.
        if(ownerName.length() > 10)
        {
            throw new BankException("Owner name must be at most 10 characters");
        }

        // Validates the mobile number using the Validator class.
        if(mobile == null || !Validator.isValidMobile(mobile))
        {
            throw new BankException("Invalid mobile number : " + mobile);
        }

        // Checks whether the opening balance is greater than zero.
        if(openingBalance <= 0)
        {
            throw new InvalidAmountException(
                "Opening balance must be greater than zero : " + openingBalance
            );
        }

        // Variable used to store the newly created account.
        Account account;

        // Creates an account according to the selected account type.
        switch(type.trim().toUpperCase())
        {
            case "SAVINGS":

                // Checks the minimum balance requirement for a savings account.
                if(openingBalance < SAVINGS_MIN_BALANCE)
                {
                    throw new InvalidAmountException(
                        "A savings account needs at least " + SAVINGS_MIN_BALANCE
                    );
                }

                // Creates a new savings account.
                account = new SavingsAccount(ownerName, openingBalance, SAVINGS_MIN_BALANCE);
                break;

            case "CURRENT":

                // Creates a new current account with an overdraft limit.
                account = new CurrentAccount(ownerName, openingBalance, CURRENT_OVERDRAFT);
                break;

            case "FD":

                // Creates a new fixed deposit account.
                account = new FixedDepositAccount(ownerName, openingBalance);
                break;

            default:

                // Throws an exception for an unsupported account type.
                throw new BankException("Unknown account type : " + type);
        }

        // Saves the newly created account in the repository.
        repository.save(account);

        return account;
    }

    // Deposits the specified amount into the given account.
    @Override
    @Audit("Deposit money")
    public void deposit(String accountNumber, long amount) throws BankException
    {
        // Finds the account using its account number.
        Account account = find(accountNumber);

        // Performs the deposit operation.
        account.deposit(amount);

        // Records the successful deposit in the transaction log.
        log("DEPOSIT", account.getAccountNumber(), amount);
    }

    // Withdraws the specified amount from the given account.
    @Override
    @Audit("Withdraw money")
    public void withdraw(String accountNumber, long amount) throws BankException
    {
        // Finds the account using its account number.
        Account account = find(accountNumber);

        // Performs the withdrawal operation.
        account.withdraw(amount);

        // Records the successful withdrawal in the transaction log.
        log("WITHDRAW", account.getAccountNumber(), amount);
    }

    // Transfers money from one account to another.
    @Override
    @Audit("Transfer money")
    public void transfer(String fromNumber, String toNumber, long amount) throws BankException
    {
        // Finds the source account.
        Account from = find(fromNumber);

        // Finds the destination account.
        Account to = find(toNumber);

        // Prevents transferring money to the same account.
        if(from.equals(to))
        {
            throw new BankException("Cannot transfer to the same account");
        }

        // Performs the transfer operation.
        from.transfer(to, amount);

        // Records the withdrawal from the source account.
        log("WITHDRAW", from.getAccountNumber(), amount);

        // Records the deposit into the destination account.
        log("DEPOSIT", to.getAccountNumber(), amount);
    }

    // Generates and returns the statement of the specified account.
    @Override
    @Audit("Show account statement")
    public String statement(String accountNumber) throws BankException
    {
        // Finds the account and formats its statement.
        return StatementFormatter.buildStatement(find(accountNumber));
    }

    // Returns all accounts sorted by account number.
    @Override
    public List<Account> listAccounts()
    {
        // Gets all accounts from the repository.
        ArrayList<Account> list = repository.findAll();

        // Sorts the accounts using their natural ordering.
        Collections.sort(list);

        return list;
    }

    // Processes multiple banking transactions as a batch.
    @Override
    @Audit("Process a batch of transactions")
    public String processBatch(List<Command> commands) throws BankException
    {
        // Checks whether any accounts are available for batch processing.
        if(repository.size() == 0)
        {
            throw new BankException("No accounts available for the batch");
        }

        // Creates a TransactionProcessor to process transactions.
        TransactionProcessor processor = new TransactionProcessor();

        // Registers all available accounts with the processor.
        for(Account account : repository.findAll())
        {
            processor.register(account);
        }

        // Stores the total balance before processing the batch.
        long before = totalBalance();

        // Submits each command to the transaction processor.
        for(Command command : commands)
        {
            processor.submitTransaction(
                command.accountNumber(), command.type(), command.amount()
            );
        }

        // Stops the processor and waits for submitted transactions to finish.
        processor.stop();

        // Gets the list of successfully completed transactions.
        List<Command> done = processor.getSucceeded();

        // Records each successful transaction in the log.
        for(Command command : done)
        {
            log(command.type().name(), command.accountNumber(), command.amount());
        }

        // Calculates the total balance after processing the batch.
        long after = totalBalance();

        // Returns a summary containing successful and failed transaction counts.
        return "Batch finished : " + done.size() + " succeeded, " +
               processor.getFailedCount() + " failed. Total balance " +
               before + " -> " + after + " (net change " + (after - before) + ")";
    }

    // Generates and returns the transaction report.
    @Override
    public List<String> report()
    {
        // Checks whether the transaction log folder exists.
        if(!Files.exists(logsFolder))
        {
            List<String> none = new ArrayList<>();
            none.add("No transactions logged yet.");
            return none;
        }

        // Generates the report using the available transaction log files.
        return ReportGenerator.generate(logsFolder, reportFile);
    }

    // Loads previously saved accounts from the data file.
    @Override
    public synchronized int loadAccounts()
    {
        // Returns zero if the account data file does not exist.
        if(!Files.exists(dataFile))
        {
            return 0;
        }

        // Loads the saved accounts from the file.
        Account[] loaded = StatePersister.load(dataFile);

        // Tracks the highest account number found in the loaded accounts.
        int highest = 0;

        // Adds each loaded account to the repository.
        for(Account account : loaded)
        {
            repository.save(account);

            try
            {
                // Extracts the numeric part of the account number.
                int number = Integer.parseInt(account.getAccountNumber().substring(2));

                // Updates the highest account number.
                highest = Math.max(highest, number);
            }
            catch(NumberFormatException e)
            {
                // Displays a message if an account number has an unexpected format.
                System.out.println(
                    "Unexpected account number : " + account.getAccountNumber()
                );
            }
        }

        // Sets the next account number after the highest loaded account number.
        Account.setNextAccountNumber(highest + 1);

        // Returns the total number of accounts loaded.
        return loaded.length;
    }

    // Saves all current accounts to the data file.
    @Override
    public synchronized void saveAccounts()
    {
        // Converts the account list into an array and saves it.
        StatePersister.save(repository.findAll().toArray(new Account[0]), dataFile);
    }
}