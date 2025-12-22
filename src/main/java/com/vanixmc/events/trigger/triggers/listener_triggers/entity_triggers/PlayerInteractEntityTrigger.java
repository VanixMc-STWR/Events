package com.vanixmc.events.trigger.triggers.listener_triggers.entity_triggers;

import com.vanixmc.events.action.domain.ActionType;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import com.vanixmc.events.util.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class PlayerInteractEntityTrigger extends ListenerTrigger {

    private final EntityType entityType;

    private final ActionType actionType;

    @Nullable
    private final String entityName;

    public PlayerInteractEntityTrigger(EntityType entityType,
                                       ActionType actionType,
                                       @Nullable String entityName) {
        this.entityType = entityType;
        this.entityName = entityName;
        this.actionType = actionType;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {

        //  to prevent firing twice.

        if (event.getHand() != EquipmentSlot.HAND) return;

        //  determine if the event's entity matches the required entity for this
        //  trigger.

        Entity interactedEntity = event.getRightClicked();

        String interactedEntityName = interactedEntity.getCustomName();

        if (interactedEntity.getType() != entityType) return;

        if (interactedEntityName != null && entityName != null &&
                !interactedEntityName.equals(entityName)) {
            return;
        }

        fire(Context.builder()
                .player(event.getPlayer())
                .location(event.getPlayer().getLocation())
                .entity(interactedEntity)
                .bukkitEvent(event)
                .build());

    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return domainConfig -> {

            String interactedEntityTypeString = domainConfig.getUppercaseString("entity-type");

            String interactedEntityNameString = domainConfig.getString("entity-name");

            String actionTypeString = domainConfig.getUppercaseString("action-type");

            EntityType interactedEntityType;

            try {

                interactedEntityType = EntityType.valueOf(interactedEntityTypeString);

            } catch (IllegalArgumentException e) {

                Chat.log("Invalid entity type argument " + interactedEntityTypeString);

                throw e;

            }

            ActionType actionType;

            try {

                actionType = ActionType.valueOf(actionTypeString);

            } catch (IllegalArgumentException e) {

                Chat.log("Invalid action type argument " + actionTypeString);

                throw e;

            }

            return new PlayerInteractEntityTrigger(interactedEntityType,
                    actionType, interactedEntityNameString);
        };
    }

}
