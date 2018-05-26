package com.n26.challenge.transaction;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.Assert.*;

public class TransactionTest {
    @Test
    public void generateIdOnConstructTest() {
        Transaction t1 = new Transaction(1, 1);
        Transaction t2 = new Transaction(1, 1);

        assertNotNull(t1);
        assertNotNull(t2);

        assertEquals(t1.getId() + 1, t2.getId());
    }

    @Test
    public void equalsTest() {
        Transaction t1 = new Transaction(1, 1);
        Transaction t2 = new Transaction(1, 1);

        assertNotEquals(t1, t2);
    }

    @Test
    public void calculateHashTest() {
        long millis = LocalDateTime.of(2000, 1, 1, 0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli();

        assertEquals(Transaction.calculateHash(millis), Transaction.calculateHash(millis - 999));
        assertEquals(Transaction.calculateHash(millis), Transaction.calculateHash(millis - 1));
        assertNotEquals(Transaction.calculateHash(millis), Transaction.calculateHash(millis - 1_000));
        assertNotEquals(Transaction.calculateHash(millis), Transaction.calculateHash(millis + 1));
    }
}
