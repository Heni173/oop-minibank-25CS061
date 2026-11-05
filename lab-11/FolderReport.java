// Program: Folder report using NIO (walkFileTree)
// For every file: relative path, size and last modified time. Summary is saved in report.txt

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FolderReport
{
    // Counters for the summary
    static int fileCount = 0;
    static int folderCount = 0;
    static long totalSize = 0;
    static String largestName = "";
    static long largestSize = -1;

    public static void main(String[] args) throws Exception
    {
        // Folder to scan
        final Path root = Paths.get("sample_folder");

        // If the folder is missing, create a small sample folder with some files
        if (!Files.exists(root))
        {
            Files.createDirectories(root.resolve("docs"));
            Files.createDirectories(root.resolve("images"));
            Files.createDirectories(root.resolve("src"));

            Files.write(root.resolve("readme.txt"), "This is the readme file".getBytes());
            Files.write(root.resolve("docs").resolve("guide.md"), "# Guide\nHow to use the project".getBytes());
            Files.write(root.resolve("docs").resolve("notes.txt"), "Some notes".getBytes());
            Files.write(root.resolve("images").resolve("logo.png"), "fake image data".getBytes());
            Files.write(root.resolve("src").resolve("App.java"), "public class App {}".getBytes());

            System.out.println("sample_folder was missing, so it was created.");
            System.out.println();
        }

        // The report text is collected here
        final StringBuilder report = new StringBuilder();

        // Format used to show the modified time
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        report.append("FOLDER REPORT: ").append(root).append("\n");
        report.append("--------------------------------------------\n");

        // Walking through the folder tree
        Files.walkFileTree(root, new SimpleFileVisitor<Path>()
        {
            // Called for every folder before its content is visited
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            {
                // The main folder itself is not counted
                if (!dir.equals(root))
                {
                    folderCount++;
                }

                return FileVisitResult.CONTINUE;
            }

            // Called for every file
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            {
                // Path of the file from the main folder, for example docs\notes.txt
                String name = root.relativize(file).toString();

                long size = attrs.size();
                String modified = format.format(new Date(attrs.lastModifiedTime().toMillis()));

                report.append(name)
                      .append(" | ")
                      .append(size)
                      .append(" bytes | ")
                      .append(modified)
                      .append("\n");

                // Updating the summary
                fileCount++;
                totalSize = totalSize + size;

                if (size > largestSize)
                {
                    largestSize = size;
                    largestName = name;
                }

                return FileVisitResult.CONTINUE;
            }
        });

        // Adding the summary at the end of the report
        report.append("--------------------------------------------\n");
        report.append("Total files   : ").append(fileCount).append("\n");
        report.append("Total folders : ").append(folderCount).append("\n");
        report.append("Total size    : ").append(totalSize).append(" bytes\n");
        report.append("Largest file  : ").append(largestName)
              .append(" (").append(largestSize).append(" bytes)\n");

        // Showing the report on the screen
        System.out.print(report);

        // Saving the report in report.txt
        Files.write(Paths.get("report.txt"), report.toString().getBytes());

        System.out.println();
        System.out.println("Report saved in report.txt");
    }
}