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
        if (this.isInTheLastMinute(transaction.getTimestamp(), currentTimestamp)) {
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
                .filter(t -> this.isInTheLastMinute(t.getTimestamp(), currentTimestamp))
                .collect(Collectors.toList());

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

    private boolean isInTheLastMinute(long timestamp, long currentTimestamp) {
        return timestamp >= currentTimestamp - 60_000 && timestamp <= currentTimestamp;
    }
}
