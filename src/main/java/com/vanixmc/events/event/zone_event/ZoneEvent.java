package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;

@Getter
public class ZoneEvent extends AbstractEvent {
    private final String regionId;

    public ZoneEvent(String id, String regionId) {
        super(id);
        this.regionId = regionId;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            String regionId = config.getString("region-id");
            return new ZoneEvent(id, regionId);
        };
    }

    @Override
    public String toString() {
        return "ZoneEvent{" +
                "id='" + getId() + '\'' +
                ", regionId='" + regionId + '\'' +
                ", running=" + isRunning() +
                ", conditionHolder=" + getConditionHolder() +
                ", actionHolder=" + getActionHolder() +
                '}';
    }
}
