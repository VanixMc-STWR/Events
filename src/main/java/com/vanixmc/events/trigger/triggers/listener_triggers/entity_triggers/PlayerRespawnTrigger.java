package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnTrigger extends ListenerTrigger {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        fire(Context.builder()
                .player(event.getPlayer())
                .bukkitEvent(event)
                .build());
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            return new PlayerRespawnTrigger();
        };
    }
}
