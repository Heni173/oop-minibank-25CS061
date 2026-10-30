import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LogAnalyzer {

    public static void main(String[] args) {
        Path folder = Paths.get("logs");
        String keyword = "ERROR";

        List<Path> files = new ArrayList<>();
        files.add(folder.resolve("app.log"));
        files.add(folder.resolve("payment.log"));
        files.add(folder.resolve("system.log"));

        try {
            Files.createDirectories(folder);

            Files.write(files.get(0), Arrays.asList(
                    "2026-10-28 10:00:01 INFO Server started",
                    "2026-10-28 10:00:05 INFO User login: amit",
                    "2026-10-28 10:01:10 ERROR Database connection failed",
                    "2026-10-28 10:01:15 INFO Retrying connection",
                    "2026-10-28 10:01:20 ERROR Database connection failed again"));

            Files.write(files.get(1), Arrays.asList(
                    "2026-10-28 11:00:00 INFO Payment request received",
                    "2026-10-28 11:00:02 INFO Payment approved",
                    "2026-10-28 11:05:30 ERROR Card declined",
                    "2026-10-28 11:06:00 INFO Receipt sent"));

            Files.write(files.get(2), Arrays.asList(
                    "2026-10-28 12:00:00 INFO Disk check ok",
                    "2026-10-28 12:10:00 WARN Memory usage high",
                    "2026-10-28 12:20:00 INFO Cleanup done"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int totalLines = 0;
        int keywordLines = 0;

        for (Path file : files) {
            int fileLines = 0;
            int fileKeywordLines = 0;

            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines++;
                    if (line.contains(keyword)) {
                        fileKeywordLines++;
                    }
                }

                BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

                System.out.println(file.getFileName()
                        + " -> size: " + attrs.size() + " bytes"
                        + ", lines: " + fileLines
                        + ", '" + keyword + "' lines: " + fileKeywordLines);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            totalLines += fileLines;
            keywordLines += fileKeywordLines;
        }

        System.out.println();
        System.out.println("Total files: " + files.size());
        System.out.println("Total lines: " + totalLines);
        System.out.println("Lines containing '" + keyword + "': " + keywordLines);
    }
}
