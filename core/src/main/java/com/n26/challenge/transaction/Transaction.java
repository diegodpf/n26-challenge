package com.n26.challenge.transaction;

public class Transaction {
    private final double amount;
    private final long timestamp;

    public Transaction(double amount, long timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return this.amount;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
