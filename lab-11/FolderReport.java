import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FolderReport {

    static Path root = Paths.get("sample_folder");
    static List<String> lines = new ArrayList<>();
    static int fileCount = 0;
    static int folderCount = 0;
    static long totalSize = 0;
    static String biggestName = "";
    static long biggestSize = 0;

    public static void main(String[] args) {
        try {
            Files.createDirectories(root.resolve("docs"));
            Files.createDirectories(root.resolve("src"));
            Files.createDirectories(root.resolve("images"));

            Files.write(root.resolve("readme.txt"), "This is a sample project".getBytes());
            Files.write(root.resolve("docs/notes.txt"), "Some notes here".getBytes());
            Files.write(root.resolve("docs/guide.md"), "# Guide".getBytes());
            Files.write(root.resolve("src/App.java"), "public class App { }".getBytes());
            Files.write(root.resolve("images/logo.png"), new byte[2048]);

            lines.add("FOLDER REPORT");
            lines.add("");

            Files.walkFileTree(root, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    if (!dir.equals(root)) {
                        folderCount++;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String name = root.relativize(file).toString();
                    long size = attrs.size();
                    String time = attrs.lastModifiedTime().toString();

                    lines.add(name + " | " + size + " bytes | " + time);

                    fileCount++;
                    totalSize = totalSize + size;

                    if (size > biggestSize) {
                        biggestSize = size;
                        biggestName = name;
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            lines.add("");
            lines.add("Total files   : " + fileCount);
            lines.add("Total folders : " + folderCount);
            lines.add("Total size    : " + totalSize + " bytes");
            lines.add("Biggest file  : " + biggestName + " (" + biggestSize + " bytes)");

            Path reportFile = Paths.get("report.txt");
            Files.write(reportFile, lines);

            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println();
            System.out.println("Report saved in report.txt");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}