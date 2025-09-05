package com.vanixmc.events.shared;

import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
public class TickTime {
    private final long duration;
    private final TimeUnit timeUnit;

    public TickTime(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public long getTickValue() {
        long seconds = TimeUnit.SECONDS.convert(duration, timeUnit);
        return seconds * 20;
    }
}
