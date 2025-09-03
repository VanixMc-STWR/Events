package com.vanixmc.events.trigger.triggers.listener_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.util.Chat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityKilledTrigger extends ListenerTrigger {
    private final EntityType killerType;
    private final EntityType victimType;

    public EntityKilledTrigger(EntityType killerType, EntityType victimType) {
        this.killerType = killerType;
        this.victimType = victimType;
    }

    @EventHandler
    public void onKill(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();
        double finalDamage = event.getFinalDamage();

        if (attacker.getType() != killerType ||
                victim.getType() != victimType) return;

        if (!(victim instanceof LivingEntity livingEntity)) return;
        if (!(livingEntity.getHealth() <= finalDamage)) return;
        fire(Context.builder()
                .location(event.getEntity().getLocation())
                .entity(event.getDamager())
                .bukkitEvent(event)
                .build());
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            String killerTypeString = config.getUppercaseString("killer-entity");
            String victimTypeString = config.getUppercaseString("victim-entity");

            EntityType killerType;
            try {
                killerType = EntityType.valueOf(killerTypeString);
            } catch (IllegalArgumentException e) {
                Chat.log("Invalid killer entity type: " + killerTypeString + ". Defaulting to PLAYER.");
                killerType = EntityType.PLAYER;
            }

            EntityType victimType;
            try {
                victimType = EntityType.valueOf(victimTypeString);
            } catch (IllegalArgumentException e) {
                Chat.log("Invalid victim entity type: " + killerTypeString + ". Defaulting to PLAYER.");
                victimType = EntityType.PLAYER;
            }


            return new EntityKilledTrigger(killerType, victimType);
        };
    }
}
