package app;

import java.util.Scanner;
import model.Account;
import service.AccountService;
import storage.AccountStorage;

// Menu class: the main program that the user runs
public class AccountApp
{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        String fileName = "accounts.dat";

        AccountService service;

        // Loading the saved accounts (if any)
        try
        {
            service = new AccountService(AccountStorage.load(fileName));
            System.out.println("Loaded " + service.getCount() + " account(s) from " + fileName);
        }
        catch (Exception e)
        {
            System.out.println("Could not load file, starting empty: " + e);
            service = new AccountService();
        }

        boolean running = true;

        while (running)
        {
            System.out.println();
            System.out.println("1. Add account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Show all accounts");
            System.out.println("5. Save and exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            try
            {
                if (choice.equals("1"))
                {
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Opening balance: ");
                    double balance = Double.parseDouble(sc.nextLine());

                    Account acc = service.addAccount(name, balance);
                    System.out.println("Account created: " + acc);
                }
                else if (choice.equals("2"))
                {
                    System.out.print("Account number: ");
                    int no = Integer.parseInt(sc.nextLine());
                    System.out.print("Amount: ");
                    double amount = Double.parseDouble(sc.nextLine());

                    service.deposit(no, amount);
                    System.out.println("Deposited. New balance: " + service.find(no).getBalance());
                }
                else if (choice.equals("3"))
                {
                    System.out.print("Account number: ");
                    int no = Integer.parseInt(sc.nextLine());
                    System.out.print("Amount: ");
                    double amount = Double.parseDouble(sc.nextLine());

                    service.withdraw(no, amount);
                    System.out.println("Withdrawn. New balance: " + service.find(no).getBalance());
                }
                else if (choice.equals("4"))
                {
                    if (service.getCount() == 0)
                    {
                        System.out.println("No accounts yet.");
                    }

                    for (Account a : service.getAccounts())
                    {
                        System.out.println(a);
                    }
                }
                else if (choice.equals("5"))
                {
                    AccountStorage.save(service.getAccounts(), fileName);
                    System.out.println("Saved " + service.getCount() + " account(s). Bye!");
                    running = false;
                }
                else
                {
                    System.out.println("Invalid choice, enter 1 to 5");
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Error: please enter a valid number");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println("Error: " + e.getMessage());
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e);
            }
        }

        sc.close();
    }
}