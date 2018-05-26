package com.n26.challenge.transaction;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Transaction {
    private static long ID_GENERATOR = 1;

    static int calculateHash(long timestamp) {
        return BigDecimal.valueOf(timestamp).divide(BigDecimal.valueOf(1_000), 0, RoundingMode.UP).intValue();
    }

    @Min(1)
    private long id;

    @NotNull
    @DecimalMin("0.0")
    private double amount;

    @NotNull
    @Min(0)
    private long timestamp;

    public Transaction() {
        this.id = ID_GENERATOR;
        ID_GENERATOR += 1;
    }

    public Transaction(double amount, long timestamp) {
        this();
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return calculateHash(this.timestamp);
    }
}
