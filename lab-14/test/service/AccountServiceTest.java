package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountServiceTest {

    private AccountService service;

    @BeforeEach
    void setUp() {
        service = new AccountService();
    }

    @Test
    void addingAccountIncreasesCount() {
        assertEquals(0, service.count());
        service.addAccount("Dhruvi", 500);
        assertEquals(1, service.count());
        service.addAccount("Amit", 0);
        assertEquals(2, service.count());
    }

    @Test
    void negativeOpeningBalanceThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addAccount("Dhruvi", -10));
    }

    @Test
    void depositToUnknownAccountThrows() {
        service.addAccount("Dhruvi", 500);
        assertThrows(IllegalArgumentException.class,
                () -> service.deposit(99, 100));
    }
}