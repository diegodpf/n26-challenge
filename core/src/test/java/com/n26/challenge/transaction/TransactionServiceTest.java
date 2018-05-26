package com.n26.challenge.transaction;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TransactionServiceTest {

    private TransactionService service;

    @Before
    public void setUp() {
        this.service = new TransactionServiceImpl();
    }

    @Test
    public void addTransactionSuccessfullyTest() {
        long currentTimestamp = this.getCurrentTimestamp();
        Transaction transaction = new Transaction(1, currentTimestamp);

        boolean success = this.service.addTransaction(transaction, currentTimestamp);

        assertTrue(success);
    }
    @Test
    public void getStatisticsSuccessfullyTest() {
        Statistics statistics = this.service.getStatistics(this.getCurrentTimestamp());

        assertNotNull(statistics);
    }

    private long getCurrentTimestamp() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
