package com.vanixmc.events.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.trigger.triggers.listener_triggers.region_trigger.RegionInteractionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public class RegionInteractionEvent extends CustomEvent {
    private final RegionInteractionType regionInteractionType;
    private final ProtectedRegion enteredRegion;
    private final Player player;
}
