package com.n26.challenge.transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

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
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, getCurrentTimestamp()));
        transactions.add(new Transaction(2, getCurrentTimestamp()));
        transactions.add(new Transaction(3, getCurrentTimestamp()));
        transactions.add(new Transaction(4, getCurrentTimestamp()));
        transactions.add(new Transaction(5, getCurrentTimestamp()));
        transactions.add(new Transaction(6, getCurrentTimestamp() - 61_000));

        when(this.repository.getTransactions()).thenReturn(transactions);

        Statistics statistics = this.service.getStatistics(getCurrentTimestamp());

        assertNotNull(statistics);
        assertEquals(15, statistics.getSum(), 0);
        assertEquals(3, statistics.getAvg(), 0);
        assertEquals(5, statistics.getMax(), 0);
        assertEquals(1, statistics.getMin(), 0);
        assertEquals(5, statistics.getCount());

        verify(this.repository).getTransactions();
    }
}
