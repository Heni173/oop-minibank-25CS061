package storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import model.Account;
import service.AccountService;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class AccountStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndReloadKeepsDataUnchanged() throws IOException {
        List<Account> original = new ArrayList<>();
        original.add(new Account(1, "Dhruvi", 500.0));
        original.add(new Account(2, "Amit", 0.0));
        original.add(new Account(3, "Riya Shah", 1234.75));

        Path file = tempDir.resolve("accounts.dat");
        AccountStorage.save(original, file);
        List<Account> loaded = AccountStorage.load(file);

        assertEquals(original, loaded);
    }

    @Test
    void loadingMissingFileGivesEmptyList() throws IOException {
        List<Account> loaded = AccountStorage.load(tempDir.resolve("missing.dat"));
        assertTrue(loaded.isEmpty());
    }

    @Test
    void reloadedServiceKeepsCountAndContinuesIds() throws IOException {
        AccountService first = new AccountService();
        first.addAccount("Dhruvi", 500);
        first.addAccount("Amit", 100);

        Path file = tempDir.resolve("accounts.dat");
        AccountStorage.save(first.getAll(), file);

        AccountService second = new AccountService();
        second.replaceAll(AccountStorage.load(file));

        assertEquals(2, second.count());
        assertEquals(3, second.addAccount("Riya", 50).getId());
    }
}