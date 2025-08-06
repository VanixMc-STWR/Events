package com.vanixmc.events.trigger.listener_triggers;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import com.vanixmc.events.trigger.trigger_modes.factory.TriggerModeFactory;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionEnterTrigger extends ListenerTrigger {
    private final String regionId;

    public RegionEnterTrigger(String id, String regionId, TriggerMode triggerMode) {
        super(id, triggerMode);
        this.regionId = regionId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (super.getSubscribers().isEmpty()) {
            unregister();
            return;
        }
        Location eventTo = event.getTo();
        if (eventTo == null) return;

        Player player = event.getPlayer();
        if (!RegionUtils.isLocationInRegion(eventTo, regionId) ||
                RegionUtils.isLocationInRegion(player.getLocation(), regionId)) return;
        fire(EventContext.builder()
                .player(player)
                .bukkitEvent(event).build());
    }

    public static ConfigBuilder<Trigger> builder() {
        return config -> {
            String id = config.getId();
            String regionId = config.getString("region-id");
            Object triggerModeObject = config.getObject("mode");

            TriggerModeFactory triggerModeFactory = EventsPlugin.getInstance()
                    .getTriggerModeFactory();
            TriggerMode triggerMode = triggerModeFactory.getTriggerMode(triggerModeObject);

            return new RegionEnterTrigger(id, regionId, triggerMode);
        };
    }

}
