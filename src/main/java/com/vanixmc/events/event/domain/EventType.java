package com.vanixmc.events.event.domain;

import com.vanixmc.events.event.zone_event.ZoneEvent;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;

@Getter
public enum EventType {
    ZONE(ZoneEvent.builder());

    private final ConfigBuilder<Event> builder;

    EventType(ConfigBuilder<Event> builder) {
        this.builder = builder;
    }
}
