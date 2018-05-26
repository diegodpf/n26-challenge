package com.n26.challenge.transaction;

import java.util.List;
import java.util.Map;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    Map<Integer, List<Transaction>> getTransactions();
    void clearTransactions();
}
