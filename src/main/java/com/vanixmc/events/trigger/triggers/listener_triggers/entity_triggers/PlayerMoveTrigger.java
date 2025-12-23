package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.Nullable;

public class PlayerMoveTrigger extends ListenerTrigger {

    @Nullable
    private final boolean onGround;
    @Nullable
    private final boolean changedBlock;

    public PlayerMoveTrigger(@Nullable boolean onGround,
                             @Nullable boolean changedBlock) {
        this.onGround = onGround;
        this.changedBlock = changedBlock;
    }

    public void onPlayerMove(PlayerMoveEvent event) {
        if (changedBlock) {

        }
        fire(Context.builder()
                .player(event.getPlayer())
                .location(event.getPlayer().getLocation())
                .bukkitEvent(event)
                .build());
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {

            boolean onGround = config.getBoolean("onGround");
            boolean changedBlock  = config.getBoolean("changedBlock");

            return new PlayerMoveTrigger(onGround, changedBlock);
        };
    }
}
