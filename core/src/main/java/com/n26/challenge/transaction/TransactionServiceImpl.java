package com.n26.challenge.transaction;

import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public boolean addTransaction(Transaction transaction, long currentTimestamp) {
        return true;
    }

    @Override
    public Statistics getStatistics(long currentTimestamp) {
        return new Statistics(0, 0, 0, 0, 0);
    }
}
