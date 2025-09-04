package com.vanixmc.events.trigger.triggers.listener_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.util.Chat;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@ToString
public class PlayerInteractTrigger extends ListenerTrigger {
    private final Location interactionLocation;
    private final Action action;

    public PlayerInteractTrigger(Location interactionLocation, Action action) {
        this.interactionLocation = interactionLocation;
        this.action = action;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!(event.getAction().equals(action))) return;

        Player player = event.getPlayer();
        if (interactionLocation != null) {
            if ((Action.RIGHT_CLICK_BLOCK.equals(event.getAction()) ||
                    Action.LEFT_CLICK_BLOCK.equals(event.getAction()))) {

                Location clickedBlockLoc = event.getClickedBlock().getLocation();
                if (clickedBlockLoc.equals(interactionLocation)) {
                    fire(Context.builder()
                            .player(player)
                            .location(clickedBlockLoc)
                            .bukkitEvent(event).build());
                }
            } else {
                Location clickedLoc = event.getClickedPosition().toLocation(player.getWorld());

                if (clickedLoc.equals(interactionLocation)) {
                    fire(Context.builder()
                            .player(player)
                            .location(clickedLoc)
                            .bukkitEvent(event)
                            .build());
                }
            }
        } else {
            fire(Context.builder()
                    .player(player)
                    .bukkitEvent(event)
                    .build());
        }
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            Location location = config.getLocation("action-location");
            String actionString = config.getUppercaseString("action");

            Action action;
            try {
                action = Action.valueOf(actionString);
            } catch (IllegalArgumentException e) {
                Chat.log("Invalid action type: " + actionString);
                throw e;
            }

            return new PlayerInteractTrigger(location, action);
        };
    }
}
