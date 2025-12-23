package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveTrigger extends ListenerTrigger {

    private final boolean onGround;

    public PlayerMoveTrigger(boolean onGround) {
        this.onGround = onGround;
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        fire(Context.builder()
                .player(event.getPlayer())
                .location(event.getPlayer().getLocation())
                .bukkitEvent(event)
                .build());
    }
}
