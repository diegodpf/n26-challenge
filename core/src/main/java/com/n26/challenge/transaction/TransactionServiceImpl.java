package com.n26.challenge.transaction;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean addTransaction(Transaction transaction, long currentTimestamp) {
        if (transaction.getTimestamp() >= this.getLastMinute(currentTimestamp)) {
            this.repository.addTransaction(transaction);
            return true;
        }

        return false;
    }

    @Override
    public Statistics getStatistics(long currentTimestamp) {
        List<Transaction> transactions = this.repository
                .getTransactions()
                .stream()
                .filter(t -> t.getTimestamp() >= this.getLastMinute(currentTimestamp))
                .collect(Collectors.toList());

        double sum = transactions.stream().map(Transaction::getAmount).reduce(0d, Double::sum);
        double avg = transactions.stream().mapToDouble(Transaction::getAmount).average().getAsDouble();
        double max = transactions.stream().map(Transaction::getAmount).max(Comparator.naturalOrder()).get();
        double min = transactions.stream().map(Transaction::getAmount).min(Comparator.naturalOrder()).get();
        long count = transactions.size();

        return new Statistics(sum, avg, max, min, count);
    }

    private long getLastMinute(long currentTimestamp) {
        return currentTimestamp - 60_000;
    }
}
