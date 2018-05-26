package com.n26.challenge.utils;

import java.util.Date;

public abstract class DateUtils {
    public static long getCurrentTimestamp() {
        return new Date().toInstant().toEpochMilli();
    }
}
