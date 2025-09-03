package com.vanixmc.events.trigger.triggers.listener_triggers.region_trigger;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import com.vanixmc.events.util.Chat;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class RegionInteractTrigger extends ListenerTrigger {
    private final RegionInteractionType interactionType;
    private final String regionId;

    public RegionInteractTrigger(String regionId, RegionInteractionType interactionType) {
        this.interactionType = interactionType;
        this.regionId = regionId;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (super.getSubscribers().isEmpty()) {
            unregister();
            return;
        }

        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        if (toLocation == null) return;

        Player player = event.getPlayer();

        boolean shouldTrigger = switch (interactionType) {
            case ENTER -> isEnteringRegion(fromLocation, toLocation);
            case EXIT -> isExitingRegion(fromLocation, toLocation);
        };

        if (!shouldTrigger) return;
        fire(Context.builder().player(player).bukkitEvent(event).build());
    }

    private boolean isEnteringRegion(Location fromLocation, Location toLocation) {
        return !isInsideRegion(fromLocation) && isInsideRegion(toLocation);
    }

    private boolean isExitingRegion(Location fromLocation, Location toLocation) {
        return isInsideRegion(fromLocation) && !isInsideRegion(toLocation);
    }

    private boolean isInsideRegion(Location location) {
        return RegionUtils.isLocationInRegion(location, regionId);
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            String regionId = config.getString("region-id");

            String typeStr = config.getUppercaseString("interaction");
            RegionInteractionType interactionType;
            try {
                interactionType = RegionInteractionType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                Chat.log("Invalid region interaction type: " + typeStr + ". Defaulting to ENTER.");
                interactionType = RegionInteractionType.ENTER;
            }

            return new RegionInteractTrigger(regionId, interactionType);
        };
    }
}