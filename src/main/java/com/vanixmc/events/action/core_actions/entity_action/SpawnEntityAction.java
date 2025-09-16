package com.vanixmc.events.action.core_actions.entity_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

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
        return false;
    }
}
