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

// Main class of the MiniBank application.
public class MiniBank
{
    // Defines the file path used to store account data.
    private static final Path DATA_FILE = Paths.get("data", "bank.dat");

    // Defines the folder path used to store transaction logs.
    private static final Path LOGS_FOLDER = Paths.get("logs");

    // Defines the file path used to save the end-of-day report.
    private static final Path REPORT_FILE = Paths.get("reports", "end-of-day.txt");

    // Defines the file path used to store audit trail records.
    private static final Path AUDIT_FILE = Paths.get("audit", "audit-trail.txt");

    // Reads a line of input from the user.
    static String readLine(Scanner sc, String prompt)
    {
        // Displays the input prompt.
        System.out.print(prompt);

        // Checks whether another input line is available.
        if(!sc.hasNextLine())
        {
            return null;
        }

        // Reads the input and removes extra spaces.
        return sc.nextLine().trim();
    }

    // Reads a whole number from the user.
    // Throws InvalidAmountException if the input is not a valid number.
    static long readNumber(Scanner sc, String prompt) throws InvalidAmountException
    {
        // Reads the number as a string.
        String text = readLine(sc, prompt);

        try
        {
            // Converts the input string into a long value.
            return Long.parseLong(text);
        }
        catch(NumberFormatException e)
        {
            // Handles invalid numeric input.
            throw new InvalidAmountException("Please enter a whole number");
        }
    }

    // Displays the main menu of the MiniBank application.
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

    // Main method: execution starts from here.
    public static void main(String[] args)
    {
        // Creates a Scanner object to accept user input.
        Scanner sc = new Scanner(System.in);

        // Creates the BankService object with the required file paths.
        BankService service =
            new BankService(DATA_FILE, LOGS_FOLDER, REPORT_FILE);

        // Wraps BankService with a dynamic proxy to support audit logging.
        BankOperations bank =
            AuditProxy.wrap(service, BankOperations.class, AUDIT_FILE);

        // Loads previously saved accounts when the application starts.
        int loaded = bank.loadAccounts();

        // Displays a message based on the number of accounts loaded.
        if(loaded == 0)
        {
            System.out.println("No saved accounts found, starting fresh.");
        }
        else
        {
            System.out.println(loaded + " saved account(s) loaded.");
        }

        // Controls the execution of the main menu loop.
        boolean running = true;

        // Repeats the menu until the user selects Save and Exit.
        while(running)
        {
            // Displays the available banking operations.
            printMenu();

            // Accepts the user's menu choice.
            String choice = readLine(sc, "Enter your choice : ");

            // Handles the case when no input is available.
            if(choice == null)
            {
                choice = "0";
            }

            try
            {
                // Executes the operation selected by the user.
                switch(choice)
                {
                    // Case 1: Open a new bank account.
                    case "1":
                    {
                        System.out.println("1. Savings   2. Current   3. Fixed Deposit");

                        // Accepts the account type choice.
                        String typeChoice = readLine(sc, "Account type : ");
                        String type;

                        // Selects Savings account.
                        if("1".equals(typeChoice))
                        {
                            type = "SAVINGS";
                        }
                        // Selects Current account.
                        else if("2".equals(typeChoice))
                        {
                            type = "CURRENT";
                        }
                        // Selects Fixed Deposit account.
                        else if("3".equals(typeChoice))
                        {
                            type = "FD";
                        }
                        else
                        {
                            // Throws an exception for an invalid account type choice.
                            throw new BankException("Please choose 1, 2 or 3");
                        }

                        // Accepts the owner's name, mobile number and opening balance.
                        String name = readLine(sc, "Owner name (max 10 characters) : ");
                        String mobile = readLine(sc, "Mobile number : ");
                        long opening = readNumber(sc, "Opening balance : ");

                        // Calls BankService to create the new account.
                        Account account = bank.openAccount(type, name, mobile, opening);

                        // Displays the details of the newly opened account.
                        System.out.println("Account opened : " + account);

                        break;
                    }

                    // Case 2: Deposit money into an account.
                    case "2":
                    {
                        // Accepts the account number and deposit amount.
                        String number = readLine(sc, "Account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        // Performs the deposit operation.
                        bank.deposit(number, amount);

                        // Displays a success message.
                        System.out.println("Deposit successful");

                        break;
                    }

                    // Case 3: Withdraw money from an account.
                    case "3":
                    {
                        // Accepts the account number and withdrawal amount.
                        String number = readLine(sc, "Account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        // Performs the withdrawal operation.
                        bank.withdraw(number, amount);

                        // Displays a success message.
                        System.out.println("Withdrawal successful");

                        break;
                    }

                    // Case 4: Transfer money between two accounts.
                    case "4":
                    {
                        // Accepts the source and destination account numbers.
                        String fromNumber = readLine(sc, "From account number : ");
                        String toNumber = readLine(sc, "To account number : ");
                        long amount = readNumber(sc, "Amount : ");

                        // Transfers the specified amount between the accounts.
                        bank.transfer(fromNumber, toNumber, amount);

                        break;
                    }

                    // Case 5: Display the statement of an account.
                    case "5":
                    {
                        // Accepts the account number.
                        String number = readLine(sc, "Account number : ");

                        // Generates and displays the account statement.
                        System.out.println(bank.statement(number));

                        break;
                    }

                    // Case 6: Display all available bank accounts.
                    case "6":
                    {
                        // Retrieves the list of all accounts.
                        List<Account> accounts = bank.listAccounts();

                        // Checks whether the account list is empty.
                        if(accounts.isEmpty())
                        {
                            System.out.println("No accounts yet.");
                        }

                        // Displays each account with its serial number.
                        for(int i = 0; i < accounts.size(); i++)
                        {
                            System.out.println((i + 1) + ". " + accounts.get(i));
                        }

                        break;
                    }

                    // Case 7: Process multiple transactions as a batch.
                    case "7":
                    {
                        // Creates a list to store transaction commands.
                        List<Command> commands = new ArrayList<>();

                        System.out.println("Enter transactions like DEPOSIT AC0001 500 (one per line).");
                        System.out.println("Press Enter on an empty line to finish.");
                        System.out.println("If you add nothing, a sample batch of 30 deposits is used.");

                        // Accepts transaction commands until an empty line is entered.
                        while(true)
                        {
                            String line = readLine(sc, "> ");

                            // Stops accepting commands when the input is empty or unavailable.
                            if(line == null || line.isEmpty())
                            {
                                break;
                            }

                            try
                            {
                                // Parses the entered transaction and adds it to the command list.
                                commands.add(
                                    CommandParser.parse(
                                        line.toUpperCase().replaceAll("\\s+", " ")
                                    )
                                );
                            }
                            catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e)
                            {
                                // Displays an error message for an incorrectly formatted command.
                                System.out.println(
                                    "Skipped, use TYPE ACCOUNT AMOUNT : " + line
                                );
                            }
                        }

                        // Creates a sample batch if the user has not entered any commands.
                        if(commands.isEmpty())
                        {
                            // Retrieves all available accounts.
                            List<Account> accounts = bank.listAccounts();

                            // Checks whether at least one account is available.
                            if(accounts.isEmpty())
                            {
                                throw new BankException("Open an account first");
                            }

                            // Adds 30 sample deposit transactions to the batch.
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

                        // Processes the batch and displays its result.
                        System.out.println(bank.processBatch(commands));

                        break;
                    }

                    // Case 8: Generate and display the end-of-day report.
                    case "8":
                    {
                        // Retrieves the report lines.
                        List<String> report = bank.report();

                        // Displays each line of the report.
                        for(String line : report)
                        {
                            System.out.println(line);
                        }

                        break;
                    }

                    // Case 9: Display the audit trail.
                    case "9":
                    {
                        // Reads the audit records from the audit file.
                        List<String> trail = AuditProxy.readTrail(AUDIT_FILE);

                        // Checks whether the audit trail is empty.
                        if(trail.isEmpty())
                        {
                            System.out.println("Audit trail is empty.");
                        }

                        // Calculates the starting index to display the last 10 records.
                        int start = Math.max(0, trail.size() - 10);

                        // Displays the latest 10 audit records.
                        for(int i = start; i < trail.size(); i++)
                        {
                            System.out.println(trail.get(i));
                        }

                        break;
                    }

                    // Case 0: Exit the application.
                    case "0":
                    {
                        // Stops the main menu loop.
                        running = false;

                        break;
                    }

                    // Handles any invalid menu choice.
                    default:
                    {
                        System.out.println("Invalid choice!! Please try again.");
                    }
                }
            }
            catch(BankException e)
            {
                // Displays the banking operation error message.
                System.out.println("Operation failed : " + e.getMessage());
            }
        }

        // Saves all account data before closing the application.
        bank.saveAccounts();

        // Displays the exit confirmation message.
        System.out.println("Accounts saved. Goodbye!");

        // Closes the Scanner.
        sc.close();
    }
}