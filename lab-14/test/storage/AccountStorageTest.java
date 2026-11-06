package storage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import model.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.AccountService;

// Tests for AccountStorage (uses a separate test file, not accounts.dat)
public class AccountStorageTest
{
    private String testFile = "test-accounts.dat";

    // Deleting the test file after every test
    @AfterEach
    public void cleanUp()
    {
        new File(testFile).delete();
    }

    // Saved accounts must come back with the same values
    @Test
    public void saveAndLoadKeepsValues() throws Exception
    {
        ArrayList<Account> list = new ArrayList<Account>();
        list.add(new Account(1001, "Heni", 1000));
        list.add(new Account(1002, "Riya", 500));

        AccountStorage.save(list, testFile);
        ArrayList<Account> loaded = AccountStorage.load(testFile);

        assertEquals(2, loaded.size());
        assertEquals("Heni", loaded.get(0).getName());
        assertEquals(500, loaded.get(1).getBalance());
    }

    // Loading a missing file must give an empty list
    @Test
    public void loadingMissingFileGivesEmptyList() throws Exception
    {
        ArrayList<Account> loaded = AccountStorage.load("no-such-file.dat");

        assertTrue(loaded.isEmpty());
    }

    // Full round trip through the service: every field must be unchanged after reload
    @Test
    public void reloadedDataIsUnchanged() throws Exception
    {
        // Building some data using the service
        AccountService service = new AccountService();
        service.addAccount("Heni", 1000);
        service.addAccount("Riya", 500.50);
        service.deposit(1001, 250);

        // Saving to the file and loading into a brand new service
        AccountStorage.save(service.getAccounts(), testFile);
        AccountService reloaded = new AccountService(AccountStorage.load(testFile));

        // Same number of accounts
        assertEquals(service.getCount(), reloaded.getCount());

        // Every account has the same account number, name and balance
        for (int i = 0; i < service.getCount(); i++)
        {
            Account before = service.getAccounts().get(i);
            Account after = reloaded.getAccounts().get(i);

            assertEquals(before.getAccountNo(), after.getAccountNo());
            assertEquals(before.getName(), after.getName());
            assertEquals(before.getBalance(), after.getBalance());

            // They are fresh objects, not the same objects
            assertNotSame(before, after);
        }

        // The next new account number continues after the reloaded ones
        assertEquals(1003, reloaded.addAccount("Neha", 100).getAccountNo());
    }
}