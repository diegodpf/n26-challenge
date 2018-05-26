package com.n26.challenge.transaction;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {
    private final Map<Integer, List<Transaction>> transactions = new HashMap<>();

    @Override
    public void addTransaction(Transaction transaction) {
        int hashcode = transaction.hashCode();

        if (this.transactions.containsKey(hashcode)) {
            this.transactions.get(hashcode).add(transaction);
        } else {
            List<Transaction> transactionsBucket = new ArrayList<>();
            transactionsBucket.add(transaction);
            this.transactions.put(hashcode, transactionsBucket);
        }
    }

    @Override
    public Map<Integer, List<Transaction>> getTransactions() {
        return this.transactions;
    }

    @Override
    public void clearTransactions() {
        this.transactions.clear();
    }
}
