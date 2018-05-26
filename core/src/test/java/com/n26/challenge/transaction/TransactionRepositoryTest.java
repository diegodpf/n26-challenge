package com.n26.challenge.transaction;

import com.n26.challenge.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransactionRepositoryTest {
    private TransactionRepository repository;

    @Before
    public void setUp() {
        this.repository = new TransactionRepositoryImpl();
    }

    @Test
    public void getTransactionsTest() {
        assertNotNull(this.repository.getTransactions());
    }

    @Test
    public void addTransactionOnEmptyBucketTest() {
        long now = DateUtils.getCurrentTimestamp();
        Transaction transaction = new Transaction(1, now);

        this.repository.addTransaction(transaction);

        Map<Integer, List<Transaction>> transactions = this.repository.getTransactions();

        assertEquals(1, transactions.size());
        assertEquals(transaction, transactions.get(transaction.hashCode()).iterator().next());
    }

    @Test
    public void addTransactionOnBucketTest() {
        long millis = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        Transaction t1 = new Transaction(1, millis);
        Transaction t2 = new Transaction(2, millis - 999);
        Transaction t3 = new Transaction(3, millis - 1_000);

        this.repository.addTransaction(t1);
        this.repository.addTransaction(t2);
        this.repository.addTransaction(t3);

        Map<Integer, List<Transaction>> transactions = this.repository.getTransactions();

        assertEquals(2, transactions.size());
        assertEquals(2, transactions.get(t1.hashCode()).size());
        assertEquals(1, transactions.get(t3.hashCode()).size());
        assertTrue(transactions.get(t1.hashCode()).contains(t1));
        assertTrue(transactions.get(t1.hashCode()).contains(t2));
        assertTrue(transactions.get(t3.hashCode()).contains(t3));
    }

    @Test
    public void clearListTest() {
        long millis = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();
        Transaction t1 = new Transaction(1, millis);
        Transaction t2 = new Transaction(2, millis - 1_000);
        Transaction t3 = new Transaction(3, millis - 2_000);

        this.repository.addTransaction(t1);
        this.repository.addTransaction(t2);
        this.repository.addTransaction(t3);

        assertEquals(3, this.repository.getTransactions().size());

        this.repository.clearTransactions();

        assertEquals(0, this.repository.getTransactions().size());
    }
}
