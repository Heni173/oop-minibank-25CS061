package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

// Tests for AccountService
public class AccountServiceTest
{
    // Adding an account must increase the count
    @Test
    public void addingIncreasesCount()
    {
        AccountService service = new AccountService();
        assertEquals(0, service.getCount());

        service.addAccount("Heni", 1000);
        assertEquals(1, service.getCount());

        service.addAccount("Riya", 500);
        assertEquals(2, service.getCount());
    }

    // A blank name is an invalid action, so it must throw an exception
    @Test
    public void blankNameThrowsException()
    {
        AccountService service = new AccountService();

        assertThrows(IllegalArgumentException.class, () -> service.addAccount("   ", 100));
        assertEquals(0, service.getCount());     // nothing was added
    }

    // Withdrawing more than the balance must throw an exception
    @Test
    public void withdrawMoreThanBalanceThrowsException()
    {
        AccountService service = new AccountService();
        int no = service.addAccount("Heni", 100).getAccountNo();

        assertThrows(IllegalArgumentException.class, () -> service.withdraw(no, 500));
        assertEquals(100, service.find(no).getBalance());   // balance unchanged
    }
}