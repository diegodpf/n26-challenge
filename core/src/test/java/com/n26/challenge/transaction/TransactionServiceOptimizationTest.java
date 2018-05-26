package com.n26.challenge.transaction;

import org.junit.AssumptionViolatedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.n26.challenge.utils.DateUtils.getCurrentTimestamp;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceOptimizationTest {
    private TransactionService service;

    private long defaultTime;
    private long setUpTime;

    @Before
    public void setUp() {
        TransactionRepositoryImpl repository = new TransactionRepositoryImpl();

        this.service = new TransactionServiceImpl(repository);

        long now = getCurrentTimestamp();
        long defaultTransactions = 50000;

        for (int i = 0; i < defaultTransactions; i++) {
            repository.addTransaction(new Transaction(i, now - i));
        }

        this.service.getStatistics(getCurrentTimestamp());
        this.service.getStatistics(getCurrentTimestamp());

        long statisticsTimeStart = this.stopwatch.runtime(TimeUnit.MILLISECONDS);
        this.service.getStatistics(getCurrentTimestamp());
        this.defaultTime = this.stopwatch.runtime(TimeUnit.MILLISECONDS) - statisticsTimeStart;

        for (int i = 0; i < defaultTransactions * 100; i++) {
            repository.addTransaction(new Transaction(i, now - i));
        }

        this.setUpTime = this.stopwatch.runtime(TimeUnit.MILLISECONDS);
    }

    private static final Logger logger = Logger.getLogger("");

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        logger.info(String.format("Test %s %s, spent %d microseconds",
                testName, status, TimeUnit.NANOSECONDS.toMicros(nanos)));
    }

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void succeeded(long nanos, Description description) {
            logInfo(description, "succeeded", nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logInfo(description, "failed", nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logInfo(description, "skipped", nanos);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
        }
    };

    @Test
    public void getStatisticsSuccessfullyTest() {
        this.service.getStatistics(getCurrentTimestamp());
        long testTime = this.stopwatch.runtime(TimeUnit.MILLISECONDS) - this.setUpTime;

        assertEquals(this.defaultTime, testTime, 20);
    }
}
