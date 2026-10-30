package service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import exception.BankException;
import exception.InvalidAmountException;
import model.Account;
import model.Command;
import model.TransactionType;
import util.CommandParser;

public class MiniBank
{
    private static final Path DATA_FILE = Paths.get("data", "bank.dat");
    private static final Path LOGS_FOLDER = Paths.get("logs");
    private static final Path REPORT_FILE = Paths.get("reports", "end-of-day.txt");
    private static final Path AUDIT_FILE = Paths.get("audit", "audit-trail.txt");

    static String readLine(Scanner sc, String prompt)
    {
        System.out.print(prompt);

        if(!sc.hasNextLine())
        {
            return null;
        }

        return sc.nextLine().trim();
    }

    static long readNumber(Scanner sc, String prompt) throws InvalidAmountException
    {
        String text = readLine(sc, prompt);

        try
        {
            return Long.parseLong(text);
        }
        catch(NumberFormatException e)
        {
            throw new InvalidAmountException("Please enter a whole number");
        }
    }

    static void printMenu()
    {
        System.out.println("\n-------------------- MiniBank --------------------");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Statement");
        System.out.println("6. List Accounts");
        System.out.println("7. Process Batch");
        System.out.println("8. End-of-Day Report");
        System.out.println("9. Audit Trail");
        System.out.println("0. Save and Exit");
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        BankService service =
            new BankService(DATA_FILE, LOGS_FOLDER, REPORT_FILE);

        BankOperations bank =
            AuditProxy.wrap(service, BankOperations.class, AUDIT_FILE);

        int loaded = bank.loadAccounts();

        if(loaded == 0)
        {
            System.out.println("No saved accounts found, starting fresh.");
        }
        else
        {
            System.out.println(loaded + " saved account(s) loaded.");
        }

        boolean running = true;

        while(running)
        {
            printMenu();

            String choice = readLine(sc, "Enter your choice : ");

            if(choice == null)
            {
                choice = "0";
            }

            try
            {
                switch(choice)
                {
                    case "1":
                    {
                        System.out.println("1. Savings   2. Current   3. Fixed Deposit");

                        String typeChoice = readLine(sc, "Account type : ");
                        String type;

                        if("1".equals(typeChoice))
                        {
                            type = "SAVINGS";
                        }
                        else if("2".equals(typeChoice))
                        {
                            type = "CURRENT";
                        }
                        else if("3".equals(typeChoice))
                        {
                            type = "FD";
                        }
                        else
                        {
                            throw new BankException("Please choose 1, 2 or 3");
                        }

                        String name = readLine(sc, "Owner name (max 10 characters) : ");
                        String mobile = readLine(sc, "Mobile number : ");
                        long opening = readNumber(sc, "Opening balance : ");

                        Account account = bank.openAccount(type, name, mobile, opening);

                        System.out.println("Account opened : " + account);

                        break;
                    }

                    case "2":
                    {
                        String number = readLine(sc, "Account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        bank.deposit(number, amount);

                        System.out.println("Deposit successful");

                        break;
                    }

                    case "3":
                    {
                        String number = readLine(sc, "Account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        bank.withdraw(number, amount);

                        System.out.println("Withdrawal successful");

                        break;
                    }

                    case "4":
                    {
                        String fromNumber = readLine(sc, "From account number : ");
                        String toNumber = readLine(sc, "To account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        bank.transfer(fromNumber, toNumber, amount);

                        break;
                    }

                    case "5":
                    {
                        String number = readLine(sc, "Account number : ");

                        System.out.println(bank.statement(number));

                        break;
                    }

                    case "6":
                    {
                        List<Account> accounts = bank.listAccounts();

                        if(accounts.isEmpty())
                        {
                            System.out.println("No accounts yet.");
                        }

                        for(int i = 0; i < accounts.size(); i++)
                        {
                            System.out.println((i + 1) + ". " + accounts.get(i));
                        }

                        break;
                    }

                    case "7":
                    {
                        List<Command> commands = new ArrayList<>();

                        System.out.println("Enter transactions like DEPOSIT AC0001 500 (one per line).");
                        System.out.println("Press Enter on an empty line to finish.");
                        System.out.println("If you add nothing, a sample batch of 30 deposits is used.");

                        while(true)
                        {
                            String line = readLine(sc, "> ");

                            if(line == null || line.isEmpty())
                            {
                                break;
                            }

                            try
                            {
                                commands.add(
                                    CommandParser.parse(
                                        line.toUpperCase().replaceAll("\\s+", " ")
                                    )
                                );
                            }
                            catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e)
                            {
                                System.out.println(
                                    "Skipped, use TYPE ACCOUNT AMOUNT : " + line
                                );
                            }
                        }

                        if(commands.isEmpty())
                        {
                            List<Account> accounts = bank.listAccounts();

                            if(accounts.isEmpty())
                            {
                                throw new BankException("Open an account first");
                            }

                            for(int i = 0; i < 30; i++)
                            {
                                commands.add(
                                    new Command(
                                        TransactionType.DEPOSIT,
                                        accounts.get(i % accounts.size()).getAccountNumber(),
                                        10
                                    )
                                );
                            }
                        }

                        System.out.println(bank.processBatch(commands));

                        break;
                    }

                    case "8":
                    {
                        List<String> report = bank.report();

                        for(String line : report)
                        {
                            System.out.println(line);
                        }

                        break;
                    }

                    case "9":
                    {
                        List<String> trail = AuditProxy.readTrail(AUDIT_FILE);

                        if(trail.isEmpty())
                        {
                            System.out.println("Audit trail is empty.");
                        }

                        int start = Math.max(0, trail.size() - 10);

                        for(int i = start; i < trail.size(); i++)
                        {
                            System.out.println(trail.get(i));
                        }

                        break;
                    }

                    case "0":
                    {
                        running = false;

                        break;
                    }

                    default:
                    {
                        System.out.println("Invalid choice!! Please try again.");
                    }
                }
            }
            catch(BankException e)
            {
                System.out.println("Operation failed : " + e.getMessage());
            }
        }

        bank.saveAccounts();

        System.out.println("Accounts saved. Goodbye!");

        sc.close();
    }
}