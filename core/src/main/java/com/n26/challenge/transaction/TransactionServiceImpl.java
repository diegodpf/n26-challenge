package com.n26.challenge.transaction;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean addTransaction(Transaction transaction, long currentTimestamp) {
        if (transaction.getTimestamp() >= currentTimestamp - 60_000 && transaction.getTimestamp() <= currentTimestamp) {
            this.repository.addTransaction(transaction);
            return true;
        }

        return false;
    }

    @Override
    public Statistics getStatistics(long currentTimestamp) {
        int hash = Transaction.calculateHash(currentTimestamp);
        Map<Integer, List<Transaction>> transactionsMap = this.repository.getTransactions();

        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            if (transactionsMap.containsKey(hash - i)) {
                transactions.addAll(transactionsMap.get(hash - i));
            }
        }

        if (transactions.isEmpty()) {
            return new Statistics();
        }

        double sum = transactions.stream().map(Transaction::getAmount).reduce(0d, Double::sum);
        long count = transactions.size();
        double avg = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP).doubleValue();
        double max = Collections.max(transactions, Comparator.comparingDouble(Transaction::getAmount)).getAmount();
        double min = Collections.min(transactions, Comparator.comparingDouble(Transaction::getAmount)).getAmount();

        return new Statistics(sum, avg, max, min, count);
    }
}
