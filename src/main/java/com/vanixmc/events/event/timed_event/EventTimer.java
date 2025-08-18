package com.vanixmc.events.event.timed_event;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.util.TimeUtils;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

@Getter
public class EventTimer extends BukkitRunnable {
    private final long runsEvery;
    private final TimeUnit timeUnit;
    private final TimedEvent timedEvent;

    public EventTimer(long runsEvery, TimeUnit timeUnit, TimedEvent timedEvent) {
        this.runsEvery = runsEvery;
        this.timeUnit = timeUnit;
        this.timedEvent = timedEvent;
    }

    @Override
    public void run() {
        timedEvent.start();
    }

    public void start() {
        this.runTaskTimerAsynchronously(EventsPlugin.getInstance(), 0, TimeUtils.toTicks(runsEvery, timeUnit));
    }
}
