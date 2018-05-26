package com.n26.challenge.transaction;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    @Override
    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}
