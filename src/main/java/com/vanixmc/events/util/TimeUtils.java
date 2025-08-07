package com.vanixmc.events.util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static long toTicks(long duration, TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(duration);
        return seconds * 20;
    }
}
