package com.n26.challenge.transaction;

public class Statistics {
    private final double sum;
    private final double avg;
    private final double max;
    private final double min;
    private final long count;

    public Statistics(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Statistics() {
        this(0, 0, 0, 0, 0);
    }

    public double getSum() {
        return this.sum;
    }

    public double getAvg() {
        return this.avg;
    }

    public double getMax() {
        return this.max;
    }

    public double getMin() {
        return this.min;
    }

    public long getCount() {
        return this.count;
    }
}
