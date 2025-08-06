package com.vanixmc.events.event.domain;

import com.vanixmc.events.trigger.domain.Triggerable;
import lombok.Builder;
import org.bukkit.entity.Player;

@Builder
public record EventContext(Player player, Event event, Triggerable triggerable, org.bukkit.event.Event bukkitEvent) {
    public static EventContext from(EventContext context, Player player, Event event, Triggerable triggerable, org.bukkit.event.Event bukkitEvent) {
        return new EventContext(
                player != null ? player : context.player(),
                event != null ? event : context.event(),
                triggerable != null ? triggerable : context.triggerable(),
                bukkitEvent != null ? bukkitEvent : context.bukkitEvent()
        );
    }
}
