package com.n26.challenge.transaction;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        double avg = transactions.stream().mapToDouble(Transaction::getAmount).average().getAsDouble();
        double max = transactions.stream().map(Transaction::getAmount).max(Comparator.naturalOrder()).get();
        double min = transactions.stream().map(Transaction::getAmount).min(Comparator.naturalOrder()).get();
        long count = transactions.size();

        return new Statistics(sum, avg, max, min, count);
    }
}
