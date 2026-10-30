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

public class BankService implements BankOperations
{
    private static final long SAVINGS_MIN_BALANCE = 1000;
    private static final long CURRENT_OVERDRAFT = 2000;

    private final Repository<Account> repository = new Repository<>();
    private final Object logLock = new Object();

    private final Path dataFile;
    private final Path logsFolder;
    private final Path reportFile;

    public BankService(Path dataFile, Path logsFolder, Path reportFile)
    {
        this.dataFile = dataFile;
        this.logsFolder = logsFolder;
        this.reportFile = reportFile;
    }

    private Account find(String accountNumber) throws AccountNotFoundException
    {
        String number =
            (accountNumber == null) ? "" : accountNumber.trim().toUpperCase();

        Account account = repository.findById(number);

        if(account == null)
        {
            throw new AccountNotFoundException(number);
        }

        return account;
    }

    private void log(String type, String accountNumber, long amount)
    {
        Path file = logsFolder.resolve("transactions-" + LocalDate.now() + ".log");

        synchronized(logLock)
        {
            TransactionLog.append(file, TransactionLog.line(type, accountNumber, amount));
        }
    }

    private long totalBalance()
    {
        long total = 0;

        for(Account account : repository.findAll())
        {
            total = total + account.getBalance();
        }

        return total;
    }

    @Override
    @Audit("Open a new account")
    public synchronized Account openAccount(String type, String ownerName, String mobile, long openingBalance) throws BankException
    {
        if(type == null)
        {
            throw new BankException("Account type is required");
        }

        if(ownerName == null || ownerName.isBlank())
        {
            throw new BankException("Owner name must not be empty");
        }

        if(ownerName.length() > 10)
        {
            throw new BankException("Owner name must be at most 10 characters");
        }

        if(mobile == null || !Validator.isValidMobile(mobile))
        {
            throw new BankException("Invalid mobile number : " + mobile);
        }

        if(openingBalance <= 0)
        {
            throw new InvalidAmountException(
                "Opening balance must be greater than zero : " + openingBalance
            );
        }

        Account account;

        switch(type.trim().toUpperCase())
        {
            case "SAVINGS":

                if(openingBalance < SAVINGS_MIN_BALANCE)
                {
                    throw new InvalidAmountException(
                        "A savings account needs at least " + SAVINGS_MIN_BALANCE
                    );
                }

                account = new SavingsAccount(ownerName, openingBalance, SAVINGS_MIN_BALANCE);
                break;

            case "CURRENT":

                account = new CurrentAccount(ownerName, openingBalance, CURRENT_OVERDRAFT);
                break;

            case "FD":

                account = new FixedDepositAccount(ownerName, openingBalance);
                break;

            default:

                throw new BankException("Unknown account type : " + type);
        }

        repository.save(account);

        return account;
    }

    @Override
    @Audit("Deposit money")
    public void deposit(String accountNumber, long amount) throws BankException
    {
        Account account = find(accountNumber);

        account.deposit(amount);

        log("DEPOSIT", account.getAccountNumber(), amount);
    }

    @Override
    @Audit("Withdraw money")
    public void withdraw(String accountNumber, long amount) throws BankException
    {
        Account account = find(accountNumber);

        account.withdraw(amount);

        log("WITHDRAW", account.getAccountNumber(), amount);
    }

    @Override
    @Audit("Transfer money")
    public void transfer(String fromNumber, String toNumber, long amount) throws BankException
    {
        Account from = find(fromNumber);
        Account to = find(toNumber);

        if(from.equals(to))
        {
            throw new BankException("Cannot transfer to the same account");
        }

        from.transfer(to, amount);

        log("WITHDRAW", from.getAccountNumber(), amount);
        log("DEPOSIT", to.getAccountNumber(), amount);
    }

    @Override
    @Audit("Show account statement")
    public String statement(String accountNumber) throws BankException
    {
        return StatementFormatter.buildStatement(find(accountNumber));
    }

    @Override
    public List<Account> listAccounts()
    {
        ArrayList<Account> list = repository.findAll();

        Collections.sort(list);

        return list;
    }

    @Override
    @Audit("Process a batch of transactions")
    public String processBatch(List<Command> commands) throws BankException
    {
        if(repository.size() == 0)
        {
            throw new BankException("No accounts available for the batch");
        }

        TransactionProcessor processor = new TransactionProcessor();

        for(Account account : repository.findAll())
        {
            processor.register(account);
        }

        long before = totalBalance();

        for(Command command : commands)
        {
            processor.submitTransaction(
                command.accountNumber(), command.type(), command.amount()
            );
        }

        processor.stop();

        List<Command> done = processor.getSucceeded();

        for(Command command : done)
        {
            log(command.type().name(), command.accountNumber(), command.amount());
        }

        long after = totalBalance();

        return "Batch finished : " + done.size() + " succeeded, " +
               processor.getFailedCount() + " failed. Total balance " +
               before + " -> " + after + " (net change " + (after - before) + ")";
    }

    @Override
    public List<String> report()
    {
        if(!Files.exists(logsFolder))
        {
            List<String> none = new ArrayList<>();
            none.add("No transactions logged yet.");
            return none;
        }

        return ReportGenerator.generate(logsFolder, reportFile);
    }

    @Override
    public synchronized int loadAccounts()
    {
        if(!Files.exists(dataFile))
        {
            return 0;
        }

        Account[] loaded = StatePersister.load(dataFile);

        int highest = 0;

        for(Account account : loaded)
        {
            repository.save(account);

            try
            {
                int number = Integer.parseInt(account.getAccountNumber().substring(2));

                highest = Math.max(highest, number);
            }
            catch(NumberFormatException e)
            {
                System.out.println(
                    "Unexpected account number : " + account.getAccountNumber()
                );
            }
        }

        Account.setNextAccountNumber(highest + 1);

        return loaded.length;
    }

    @Override
    public synchronized void saveAccounts()
    {
        StatePersister.save(repository.findAll().toArray(new Account[0]), dataFile);
    }
}