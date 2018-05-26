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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
    public void getStatisticsSuccessfullyTest() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, getCurrentTimestamp()));

        when(this.repository.getTransactions()).thenReturn(transactions);

        Statistics statistics = this.service.getStatistics(getCurrentTimestamp());

        assertNotNull(statistics);

        verify(this.repository).getTransactions();
    }
}
