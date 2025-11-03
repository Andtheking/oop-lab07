package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test class for the {@link StrictBankAccount} class.
 */
class TestStrictBankAccount {
    private static final double AMOUNT = 100;
    private static final int ID_ACCOUNT = 1;

    // Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    /**
     * Prepare the tests.
     */
    @BeforeEach
    public void setUp() {
        mRossi = new AccountHolder("Mario", "Rossi", ID_ACCOUNT);
        bankAccount = new StrictBankAccount(mRossi, 0);
    }

    /**
     * Test the initial state of the StrictBankAccount.
     */
    @Test
    public void testInitialization() {
        assertEquals(0,bankAccount.getBalance());
        assertEquals(0,bankAccount.getTransactionsCount());
        assertEquals(mRossi, bankAccount.getAccountHolder());
    }

    /**
     * Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
     */
    @Test
    public void testManagementFees() {
        bankAccount.deposit(ID_ACCOUNT, AMOUNT);
        final double expected = AMOUNT - (StrictBankAccount.MANAGEMENT_FEE + bankAccount.getTransactionsCount() * StrictBankAccount.TRANSACTION_FEE);
        assertEquals(AMOUNT, bankAccount.getBalance());
        assertEquals(1, bankAccount.getTransactionsCount());
        bankAccount.chargeManagementFees(ID_ACCOUNT);
        assertEquals(bankAccount.getBalance(), expected);
    }

    /**
     * Test that withdrawing a negative amount causes a failure.
     */
    @Test
    public void testNegativeWithdraw() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bankAccount.withdraw(ID_ACCOUNT, -AMOUNT);
            }
        });
    }

    /**
     * Test that withdrawing more money than it is in the account is not allowed.
     */
    @Test
    public void testWithdrawingTooMuch() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                bankAccount.withdraw(ID_ACCOUNT, AMOUNT);
            }
        });
    }
}
