package com.vanixmc.events.listeners;

import com.vanixmc.events.event.factory.EventFactory;
import com.vanixmc.events.events.RegionInteractionEvent;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@AllArgsConstructor
public class RegionInteractListener implements Listener {
    private final EventFactory eventFactory;

    @EventHandler
    public void onInteract(RegionInteractionEvent event) {
        switch (event.getRegionInteractionType()) {
            case ENTER -> eventFactory.getZoneEvents()
                    .forEach(zoneEvent -> {
                        if (!zoneEvent.getRegionId().equals(event.getEnteredRegion().getId())) return;
                        zoneEvent.onEnter(event.getPlayer().getUniqueId());
                    });
            case EXIT -> eventFactory.getZoneEvents()
                    .forEach(zoneEvent -> {
                        if (!zoneEvent.getRegionId().equals(event.getEnteredRegion().getId())) return;
                        zoneEvent.onExit(event.getPlayer().getUniqueId());
                    });
        }
    }
}
