package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportGenerator
{
    // Reads all .log files from logsFolder and builds an end of day report.
    // The report is saved to reportFile and also returned as a list of lines,
    // so the caller can print it on the console.
    public static List<String> generate(Path logsFolder, Path reportFile)
    {
        // Format used for the "Generated" time (current date and time)
        DateTimeFormatter nowFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // File times come as Instant (no time zone), so a zone is needed
        // before they can be shown as normal date and time
        DateTimeFormatter fileFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                             .withZone(ZoneId.systemDefault());

        // lines : every line of the final report
        // files : the log files found in the folder
        List<String> lines = new ArrayList<>();
        List<Path> files = new ArrayList<>();

        // Step 1 : find the log files.
        // "*.log" is a glob pattern, so only files ending with .log are returned.
        // try-with-resources closes the directory stream automatically.
        try(DirectoryStream<Path> stream =
                Files.newDirectoryStream(logsFolder, "*.log"))
        {
            for(Path path : stream)
            {
                // Skip sub folders, keep only real files
                if(Files.isRegularFile(path))
                {
                    files.add(path);
                }
            }
        }
        catch(IOException e)
        {
            // Folder missing or unreadable : report the problem and stop here
            lines.add("Could not read logs folder : " + e.getMessage());
            return lines;
        }

        // The order of files from the directory is not guaranteed,
        // so sort them to get the same report every time
        Collections.sort(files);

        // Counters used for the summary at the end
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int depositCount = 0;
        int withdrawCount = 0;
        int totalLines = 0;
        int badLines = 0;

        // Step 2 : report header and table column titles
        // %-14s = left aligned in 14 characters, %9s = right aligned in 9 characters
        lines.add("END OF DAY REPORT");
        lines.add("Generated   : " + LocalDateTime.now().format(nowFormat));
        lines.add("Logs folder : " + logsFolder);
        lines.add("");
        lines.add(String.format("%-14s %9s  %-19s  %s",
                "FILE", "SIZE", "LAST MODIFIED", "LINES"));
        lines.add(String.format("%-14s %9s  %-19s  %s",
                "----", "----", "-------------", "-----"));

        // Step 3 : read every log file one by one
        for(Path path : files)
        {
            // Number of valid (non blank) lines in this file only
            int fileLines = 0;

            try(BufferedReader reader = Files.newBufferedReader(path))
            {
                String line;

                // readLine() returns null at the end of the file
                while((line = reader.readLine()) != null)
                {
                    // Blank lines are ignored and not counted anywhere
                    if(line.isBlank())
                    {
                        continue;
                    }

                    fileLines++;
                    totalLines++;

                    // A valid log line has exactly 4 parts separated by commas.
                    // parts[1] is the transaction type and parts[3] is the amount.
                    // Any other format is counted as a bad line and skipped.
                    String[] parts = line.split(",");

                    if(parts.length != 4)
                    {
                        badLines++;
                        continue;
                    }

                    try
                    {
                        // parseLong throws NumberFormatException if the amount is not a number
                        long amount = Long.parseLong(parts[3].trim());
                        String type = parts[1].trim();

                        // Add the amount to the matching total
                        if(type.equals("DEPOSIT"))
                        {
                            totalDeposits = totalDeposits + amount;
                            depositCount++;
                        }
                        else if(type.equals("WITHDRAW"))
                        {
                            totalWithdrawals = totalWithdrawals + amount;
                            withdrawCount++;
                        }
                        else
                        {
                            // Unknown transaction type
                            badLines++;
                        }
                    }
                    catch(NumberFormatException e)
                    {
                        // Amount is not a valid number
                        badLines++;
                    }
                }

                // File size and last modified time are read from the file attributes
                BasicFileAttributes attrs =
                    Files.readAttributes(path, BasicFileAttributes.class);

                // One table row for this file : name, size in bytes, last modified, line count
                lines.add(String.format("%-14s %7d B  %-19s  %d",
                        path.getFileName(),
                        attrs.size(),
                        fileFormat.format(attrs.lastModifiedTime().toInstant()),
                        fileLines));
            }
            catch(IOException e)
            {
                // One unreadable file should not stop the whole report
                lines.add(path.getFileName() + " could not be read : " + e.getMessage());
            }
        }

        // Step 4 : summary of everything read from all files
        // Net change = money that came in minus money that went out
        lines.add("");
        lines.add("SUMMARY");
        lines.add("Files read          : " + files.size());
        lines.add("Total lines         : " + totalLines);
        lines.add("Deposits            : " + depositCount + " transactions, total " + totalDeposits);
        lines.add("Withdrawals         : " + withdrawCount + " transactions, total " + totalWithdrawals);
        lines.add("Net change          : " + (totalDeposits - totalWithdrawals));
        lines.add("Bad lines skipped   : " + badLines);

        // Step 5 : save the report to a file
        try
        {
            // Create the report folder first if it does not exist yet
            if(reportFile.getParent() != null)
            {
                Files.createDirectories(reportFile.getParent());
            }

            // Writes each item of the list as one line in the file
            Files.write(reportFile, lines);
        }
        catch(IOException e)
        {
            // Saving failed, but the report is still returned so it can be printed
            lines.add("Could not write report : " + e.getMessage());
        }

        return lines;
    }
}