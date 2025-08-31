package com.vanixmc.events.listeners;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.vanixmc.events.events.RegionInteractionEvent;
import com.vanixmc.events.trigger.triggers.listener_triggers.region_trigger.RegionInteractionType;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
        if (toLocation == null) return;

        Player player = event.getPlayer();
        ApplicableRegionSet regionsGoingInto = RegionUtils.getRegionsByLocation(toLocation);
        if (regionsGoingInto != null) {
            triggerRegionEnterEvent(player, regionsGoingInto);
        }

        ApplicableRegionSet regionsCurrentlyIn = RegionUtils.getRegionsByLocation(fromLocation);
        if (regionsCurrentlyIn != null) {
            triggerRegionExitEvent(player, toLocation, regionsCurrentlyIn);
        }
    }

    private void triggerRegionEnterEvent(Player player, ApplicableRegionSet regions) {
        regions.getRegions()
                .forEach(region -> new RegionInteractionEvent(RegionInteractionType.ENTER,
                        region, player).call());
    }

    private void triggerRegionExitEvent(Player player, Location toLocation, ApplicableRegionSet regions) {
        regions.getRegions()
                .forEach(region -> {
                    if (RegionUtils.isLocationInRegion(toLocation, region)) return;
                    new RegionInteractionEvent(RegionInteractionType.EXIT, region, player)
                            .call();
                });
    }
}