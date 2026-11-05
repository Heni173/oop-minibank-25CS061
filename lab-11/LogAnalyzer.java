// Program: Log analyzer using Path and Files

import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class LogAnalyzer
{
    // Totals for all files
    static int totalLines = 0;
    static int totalMatches = 0;
    static long totalSize = 0;

    // Creates a text file with the given lines
    static void createFile(Path file, String[] lines) throws Exception
    {
        Files.write(file, Arrays.asList(lines));
    }

    // Reads a file line by line, counts lines and keyword matches, prints its size
    static void analyzeFile(Path file, String keyword) throws Exception
    {
        // Reading all lines of the file
        List<String> lines = Files.readAllLines(file);

        int matches = 0;

        // Checking every line for the keyword (ignoring upper/lower case)
        for (String line : lines)
        {
            if (line.toLowerCase().contains(keyword.toLowerCase()))
            {
                matches++;
            }
        }

        // Getting the size of the file from its attributes
        BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);
        long size = attr.size();

        System.out.println("File: " + file.getFileName());
        System.out.println("  Lines   : " + lines.size());
        System.out.println("  Matches : " + matches);
        System.out.println("  Size    : " + size + " bytes");
        System.out.println();

        // Adding to the totals
        totalLines = totalLines + lines.size();
        totalMatches = totalMatches + matches;
        totalSize = totalSize + size;
    }

    public static void main(String[] args) throws Exception
    {
        // Creating the logs folder (nothing happens if it already exists)
        Path folder = Paths.get("logs");
        Files.createDirectories(folder);

        // Lines of each log file
        String[] appLines = {
            "INFO App started",
            "ERROR Database connection failed",
            "INFO User login ok",
            "WARN Disk space low",
            "ERROR Null pointer in module"
        };

        String[] paymentLines = {
            "INFO Payment received 500",
            "ERROR Payment failed for order 12",
            "INFO Refund issued"
        };

        String[] systemLines = {
            "INFO System booted",
            "WARN High memory usage",
            "INFO Backup completed",
            "ERROR Service crashed",
            "INFO Shutdown complete"
        };

        // Creating the three text files inside the logs folder
        Path appFile = folder.resolve("app.log");
        Path paymentFile = folder.resolve("payment.log");
        Path systemFile = folder.resolve("system.log");

        createFile(appFile, appLines);
        createFile(paymentFile, paymentLines);
        createFile(systemFile, systemLines);

        // Keyword to search
        String keyword = "error";

        System.out.println("Keyword: " + keyword);
        System.out.println();

        // Analyzing each file
        analyzeFile(appFile, keyword);
        analyzeFile(paymentFile, keyword);
        analyzeFile(systemFile, keyword);

        // Printing the totals
        System.out.println("Total lines : " + totalLines);
        System.out.println("Total lines containing \"" + keyword + "\" : " + totalMatches);
        System.out.println("Total size  : " + totalSize + " bytes");
    }
}