package util;

import model.Command;
import model.TransactionType;

// Utility class for parsing transaction commands.
public class CommandParser
{
    // Parses the input line and creates a Command object.
    public static Command parse(String line)
    {
        String[] parts = line.split(" ");

        // Converts the first part into a transaction type.
        TransactionType type =
            TransactionType.valueOf(parts[0]);

        // Extracts the account number from the second part.
        String accountNumber = parts[1];

        // Converts the third part into a long amount.
        long amount = Long.parseLong(parts[2]);

        return new Command(type, accountNumber, amount);
    }
}