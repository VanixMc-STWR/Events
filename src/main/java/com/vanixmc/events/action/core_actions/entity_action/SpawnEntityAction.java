package com.vanixmc.events.action.core_actions.entity_action;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.util.EntityUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SpawnEntityAction extends AbstractAction {
    @Nullable
    private final EntityType entityType;
    @Nullable
    private final String name;
    private final Location location;
    private final int amount;

    public SpawnEntityAction(@Nullable EntityType entityType, @Nullable String name, Location location, int amount) {
        this.entityType = entityType;
        this.name = name;
        this.location = location;
        this.amount = amount;
    }

    //TODO: Constructor for MythicMobs Id here>>>

    @Override
    public boolean execute(Context context) {
        World contextWorld = context.getLocation().getWorld();

        if (!Objects.equals(location.getWorld(), contextWorld)) return false;

        if (!entityType.isRegistered()) return false;

        for (int i = 0; i < amount; i++) {
            contextWorld.spawn(location, entityType.getEntityClass());
        }

        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String entityTypeString = config.getString("entity_type");

            if (!EntityUtils.isValidEntityType(entityTypeString)) return false;

            EntityType entityType = EntityType.
        };
    }
}
