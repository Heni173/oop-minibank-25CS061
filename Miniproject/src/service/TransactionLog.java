package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Helper class for writing transaction log files
public class TransactionLog
{
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Builds one log line : time,type,accountNumber,amount
    // ReportGenerator reads this same 4 part format (type = parts[1], amount = parts[3])
    public static String line(String type, String accountNumber, long amount)
    {
        return LocalDateTime.now().format(FORMAT) +
               "," + type +
               "," + accountNumber +
               "," + amount;
    }

    // Adds one line at the end of the file, old lines are kept
    public static void append(Path file, String line)
    {
        try
        {
            // Create the folder first if it does not exist
            if(file.getParent() != null)
            {
                Files.createDirectories(file.getParent());
            }

            // CREATE = make the file if missing, APPEND = write at the end instead of overwriting
            Files.write(
                file,
                List.of(line),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
            );
        }
        catch(IOException e)
        {
            System.out.println("Could not write log : " + e.getMessage());
        }
    }

    // Deletes the log file so a new run starts fresh (no error if the file does not exist)
    public static void clear(Path file)
    {
        try
        {
            Files.deleteIfExists(file);
        }
        catch(IOException e)
        {
            System.out.println("Could not delete log : " + e.getMessage());
        }
    }
}