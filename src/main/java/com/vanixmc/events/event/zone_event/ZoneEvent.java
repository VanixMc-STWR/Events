package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// REDUCES GLOBAL SCOPE OF ACTIONS TO A LIST OF PLAYERS/ENTITIES IN ZONE
@Getter
public class ZoneEvent extends AbstractEvent {
    private final Set<UUID> playerUUIDsInZone;
    private final String regionId;

    public ZoneEvent(String id, String regionId) {
        super(id);
        this.playerUUIDsInZone = new HashSet<>();
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

    @Override
    public boolean execute(Context context) {
        if (context.getPlayer() == null) {
            for (Player player : getPlayersInZone()) {
                Context playerContext = new Context(context);
                playerContext.setPlayer(player);
                return super.execute(playerContext);
            }
        }
        return super.execute(context);
    }

    public void onEnter(UUID playerUUID) {
        playerUUIDsInZone.add(playerUUID);
    }

    public void onExit(UUID playerUUID) {
        playerUUIDsInZone.remove(playerUUID);
    }

    public List<Player> getPlayersInZone() {
        return playerUUIDsInZone
                .stream()
                .map(Bukkit::getPlayer)
                .toList();
    }

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            String regionId = config.getString("region-id");
            return new ZoneEvent(id, regionId);
        };
    }
}
