package com.n26.challenge.transaction;

public interface TransactionService {
    boolean addTransaction(Transaction transaction, long currentTimestamp);
    Statistics getStatistics(long currentTimestamp);
}
