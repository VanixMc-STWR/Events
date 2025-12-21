package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.action.domain.ActionType;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityTrigger extends ListenerTrigger {

    private final EntityType interactedEntityType;

    private final ActionType actionType;

    private final String interactedEntityName;

    public PlayerInteractEntityTrigger(EntityType interactedEntityType,
                                       ActionType actionType,
                                       String interactedEntityName) {
        this.interactedEntityType = interactedEntityType;
        this.interactedEntityName = interactedEntityName;
        this.actionType = actionType;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {

        Entity interactedEntity = event.getRightClicked();

        //  determine if the event's entity matches the required entity for this
        //  trigger.

        if (interactedEntity.getType() != interactedEntityType
            || !interactedEntity.getName().equals(interactedEntityName)) {
            return;
        }

        //  TODO: determine the action type correspondence with the event.

        fire(Context.builder()
                .player(event.getPlayer())
                .location(event.getPlayer().getLocation())
                .entity(interactedEntity)
                .bukkitEvent(event)
                .build());

    }

}
