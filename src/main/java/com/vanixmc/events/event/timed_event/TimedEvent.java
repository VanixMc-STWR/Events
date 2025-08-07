package com.vanixmc.events.event.timed_event;

import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;

import java.util.concurrent.TimeUnit;

// Todo: Resubscribe logic
public class TimedEvent extends AbstractEvent {
    private final EventTimer eventTimer;

    public TimedEvent(String id, long runsEvery, TimeUnit timeUnit) {
        super(id);
        this.eventTimer = new EventTimer(runsEvery, timeUnit, this);
        this.eventTimer.start();
    }

    @Override
    public boolean start() {
        if (isRunning()) return true;
        setRunning(true);
        return true;
    }

    @Override
    public boolean stop() {
        return false;
    }

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            Long runsEvery = config.getLong("runs-every");
            TimeUnit timeUnit = TimeUnit.valueOf(config.getUppercaseString("time-unit"));

            return new TimedEvent(id, runsEvery, timeUnit);
        };
    }
}
