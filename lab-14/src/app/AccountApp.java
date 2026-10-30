package app;

import model.Account;
import service.AccountService;
import storage.AccountStorage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class AccountApp {

    public static void main(String[] args) {
        AccountService service = new AccountService();
        Scanner scanner = new Scanner(System.in);
        Path file = Paths.get("accounts.dat");

        try {
            service.replaceAll(AccountStorage.load(file));
            System.out.println("Loaded " + service.count() + " account(s).");
        } catch (IOException e) {
            System.out.println("Could not load data: " + e.getMessage());
        }

        while (true) {
            System.out.println();
            System.out.println("1. Add account");
            System.out.println("2. Deposit");
            System.out.println("3. List accounts");
            System.out.println("0. Save and exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                try {
                    AccountStorage.save(service.getAll(), file);
                    System.out.println("Saved " + service.count() + " account(s).");
                } catch (IOException e) {
                    System.out.println("Could not save data: " + e.getMessage());
                }
                System.out.println("Goodbye.");
                break;
            }

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Holder name: ");
                        String name = scanner.nextLine();
                        System.out.print("Opening balance: ");
                        double opening = Double.parseDouble(scanner.nextLine().trim());
                        Account created = service.addAccount(name, opening);
                        System.out.println("Created: " + created);
                        break;
                    case "2":
                        System.out.print("Account id: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Amount: ");
                        double amount = Double.parseDouble(scanner.nextLine().trim());
                        service.deposit(id, amount);
                        System.out.println("Deposited. " + service.findById(id));
                        break;
                    case "3":
                        System.out.println("Total accounts: " + service.count());
                        for (Account account : service.getAll()) {
                            System.out.println(account);
                        }
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}