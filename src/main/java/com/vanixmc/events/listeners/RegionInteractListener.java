package com.vanixmc.events.listeners;

import com.vanixmc.events.event.factory.EventFactory;
import com.vanixmc.events.events.RegionInteractionEvent;
import com.vanixmc.events.trigger.impl.listener_triggers.region_trigger.RegionInteractionType;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class RegionInteractListener implements Listener {
    private final EventFactory eventFactory;

    @EventHandler
    public void onInteract(RegionInteractionEvent event) {
        if (RegionInteractionType.ENTER.equals(event.getRegionInteractionType())) {
            eventFactory.getZoneEvents()
                    .forEach(zoneEvent -> zoneEvent.onEnter(event.getPlayer().getUniqueId()));
            return;
        }

        if (RegionInteractionType.EXIT.equals(event.getRegionInteractionType())) {
            eventFactory.getZoneEvents()
                    .forEach(zoneEvent -> zoneEvent.onExit(event.getPlayer().getUniqueId()));
        }
    }
}
