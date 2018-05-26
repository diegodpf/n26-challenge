package com.n26.challenge.transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.n26.challenge.utils.DateUtils.getCurrentTimestamp;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {

    private TransactionService service;

    @Mock
    private TransactionRepository repository;

    @Before
    public void setUp() {
        this.service = new TransactionServiceImpl(this.repository);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(this.repository);
    }

    @Test
    public void addTransactionSuccessfullyTest() {
        long currentTimestamp = getCurrentTimestamp();
        Transaction transaction = new Transaction(1, currentTimestamp);

        boolean success = this.service.addTransaction(transaction, currentTimestamp);

        assertTrue(success);

        verify(this.repository).addTransaction(transaction);
    }

    @Test
    public void addOldTransactionTest() {
        long currentTimestamp = getCurrentTimestamp();
        Transaction transaction = new Transaction(1, currentTimestamp - 61_000);

        boolean success = this.service.addTransaction(transaction, currentTimestamp);

        assertFalse(success);
    }

    @Test
    public void getStatisticsSuccessfullyTest() {
        long millis = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();

        Transaction t1 = new Transaction(1, millis - 1_000);
        Transaction t2 = new Transaction(2, millis - 2_000);
        Transaction t3 = new Transaction(3, millis - 30_000);
        Transaction t4 = new Transaction(4, millis - 45_000);
        Transaction t5 = new Transaction(5, millis - 59_000);
        Transaction t6 = new Transaction(6, millis - 61_000);

        Map<Integer, List<Transaction>> transactions = new HashMap<>();
        transactions.put(t1.hashCode(), Collections.singletonList(t1));
        transactions.put(t2.hashCode(), Collections.singletonList(t2));
        transactions.put(t3.hashCode(), Collections.singletonList(t3));
        transactions.put(t4.hashCode(), Collections.singletonList(t4));
        transactions.put(t5.hashCode(), Collections.singletonList(t5));
        transactions.put(t6.hashCode(), Collections.singletonList(t6));

        when(this.repository.getTransactions()).thenReturn(transactions);

        Statistics statistics = this.service.getStatistics(millis);

        assertNotNull(statistics);
        assertEquals(5, statistics.getCount());
        assertEquals(15, statistics.getSum(), 0);
        assertEquals(3, statistics.getAvg(), 0);
        assertEquals(5, statistics.getMax(), 0);
        assertEquals(1, statistics.getMin(), 0);

        verify(this.repository).getTransactions();
    }
}
