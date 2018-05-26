package com.n26.challenge.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public abstract class DateUtils {
    public static long getCurrentTimestamp() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
