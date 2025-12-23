package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import com.vanixmc.events.util.Chat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.Nullable;

import static com.vanixmc.events.shared.DomainConfig.isValidBoolean;

public class PlayerJoinTrigger extends ListenerTrigger {

    @Nullable
    private final boolean firstJoin;

    public PlayerJoinTrigger(@Nullable boolean firstJoin) {
        this.firstJoin = firstJoin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (firstJoin) {
            if (event.getPlayer().hasPlayedBefore()) return;
        }

        fire(Context.builder()
                .player(event.getPlayer())
                .location(event.getPlayer().getLocation())
                .bukkitEvent(event)
                .build());
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {

            String firstJoinArg = config.getString("firstJoin");

            boolean firstJoin = false;

            if (!firstJoinArg.isEmpty()) {

                boolean isBool = isValidBoolean(firstJoinArg);

                if (isBool) firstJoin = Boolean.parseBoolean(firstJoinArg);

                else {
                    Chat.log("Invalid argument for 'firstJoin' - use 'true' or 'false'");
                    return null;
                }
            }

            return new PlayerJoinTrigger(firstJoin);
        };
    }
}
