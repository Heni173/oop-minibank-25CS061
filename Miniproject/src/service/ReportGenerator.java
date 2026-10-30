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
    public static List<String> generate(Path logsFolder, Path reportFile)
    {
        DateTimeFormatter nowFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        DateTimeFormatter fileFormat =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                             .withZone(ZoneId.systemDefault());

        List<String> lines = new ArrayList<>();
        List<Path> files = new ArrayList<>();

        try(DirectoryStream<Path> stream =
                Files.newDirectoryStream(logsFolder, "*.log"))
        {
            for(Path path : stream)
            {
                if(Files.isRegularFile(path))
                {
                    files.add(path);
                }
            }
        }
        catch(IOException e)
        {
            lines.add("Could not read logs folder : " + e.getMessage());
            return lines;
        }

        Collections.sort(files);

        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int depositCount = 0;
        int withdrawCount = 0;
        int totalLines = 0;
        int badLines = 0;

        lines.add("END OF DAY REPORT");
        lines.add("Generated   : " + LocalDateTime.now().format(nowFormat));
        lines.add("Logs folder : " + logsFolder);
        lines.add("");
        lines.add(String.format("%-14s %9s  %-19s  %s",
                "FILE", "SIZE", "LAST MODIFIED", "LINES"));
        lines.add(String.format("%-14s %9s  %-19s  %s",
                "----", "----", "-------------", "-----"));

        for(Path path : files)
        {
            int fileLines = 0;

            try(BufferedReader reader = Files.newBufferedReader(path))
            {
                String line;

                while((line = reader.readLine()) != null)
                {
                    if(line.isBlank())
                    {
                        continue;
                    }

                    fileLines++;
                    totalLines++;

                    String[] parts = line.split(",");

                    if(parts.length != 4)
                    {
                        badLines++;
                        continue;
                    }

                    try
                    {
                        long amount = Long.parseLong(parts[3].trim());
                        String type = parts[1].trim();

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
                            badLines++;
                        }
                    }
                    catch(NumberFormatException e)
                    {
                        badLines++;
                    }
                }

                BasicFileAttributes attrs =
                    Files.readAttributes(path, BasicFileAttributes.class);

                lines.add(String.format("%-14s %7d B  %-19s  %d",
                        path.getFileName(),
                        attrs.size(),
                        fileFormat.format(attrs.lastModifiedTime().toInstant()),
                        fileLines));
            }
            catch(IOException e)
            {
                lines.add(path.getFileName() + " could not be read : " + e.getMessage());
            }
        }

        lines.add("");
        lines.add("SUMMARY");
        lines.add("Files read          : " + files.size());
        lines.add("Total lines         : " + totalLines);
        lines.add("Deposits            : " + depositCount + " transactions, total " + totalDeposits);
        lines.add("Withdrawals         : " + withdrawCount + " transactions, total " + totalWithdrawals);
        lines.add("Net change          : " + (totalDeposits - totalWithdrawals));
        lines.add("Bad lines skipped   : " + badLines);

        try
        {
            if(reportFile.getParent() != null)
            {
                Files.createDirectories(reportFile.getParent());
            }

            Files.write(reportFile, lines);
        }
        catch(IOException e)
        {
            lines.add("Could not write report : " + e.getMessage());
        }

        return lines;
    }
}