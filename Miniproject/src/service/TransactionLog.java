package service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionLog
{
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String line(String type, String accountNumber, long amount)
    {
        return LocalDateTime.now().format(FORMAT) +
               "," + type +
               "," + accountNumber +
               "," + amount;
    }

    public static void append(Path file, String line)
    {
        try
        {
            if(file.getParent() != null)
            {
                Files.createDirectories(file.getParent());
            }

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