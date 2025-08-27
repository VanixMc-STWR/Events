package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

// TODO: REDO, SPECIFICALLY TO REDUCE GLOBAL SCOPE TO A LIST OF PLAYERS/ENTITIES IN ZONE
@Getter
@ToString
public class ZoneEvent extends AbstractEvent {
    private final Set<UUID> playersInZone;
    private final String regionId;

    public ZoneEvent(String id, String regionId) {
        super(id);
        this.playersInZone = new HashSet<>();
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

    public void onEnter(UUID playerUUID) {
        playersInZone.add(playerUUID);
    }

    public void onExit(UUID playerUUID) {
        playersInZone.remove(playerUUID);
    }

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            String regionId = config.getString("region-id");
            return new ZoneEvent(id, regionId);
        };
    }
}
