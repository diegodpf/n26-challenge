package com.n26.challenge.transaction;

import java.util.List;

public interface TransactionRepository {
    void addTransaction(Transaction transaction);
    List<Transaction> getTransactions();
}
