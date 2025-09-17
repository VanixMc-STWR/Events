package com.vanixmc.events.action.core_actions.entity_action;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.util.EntityUtils;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.synth.Region;
import java.util.Objects;
import java.util.Optional;

public class SpawnEntityAction extends AbstractAction {
    private final EntityType entityType;
    @Nullable
    private final String name;
    private final Location location;
    private final int amount;

    public SpawnEntityAction(EntityType entityType, @Nullable String name, Location location, int amount) {
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
            String worldName = config.getString("world");
            if (worldName.isEmpty()) {
                throw new IllegalArgumentException("world-name cannot be null or empty.");
            }

            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                throw new IllegalArgumentException("Invalid world-name value; no corresponding world.");
            }

            String regionId = config.getString("region-id");
            if (regionId.isEmpty()) {
                throw new IllegalArgumentException("region-id cannot be null or empty.");
            }

            Optional<ProtectedRegion> region = RegionUtils.getRegionById(world, regionId);
            if (region.isEmpty()) {
                throw new IllegalArgumentException("Invalid region-id value; no corresponding region.");
            }

            //  TODO: Determine error handling.
            Location location = RegionUtils.getLocationByRegion(world, region.get());

            String entityTypeString = config.getString("entity_type");

            if (!EntityUtils.isValidEntityType(entityTypeString)) {
                throw new IllegalArgumentException("Invalid entity type.");
            }

            EntityType entityType = EntityType.valueOf(entityTypeString
                    .toUpperCase().trim());

            int amount = config.getInt("amount");

            String customName = null;

            if (config.getConfig().containsKey("name")) {
                customName = config.getString("name");
            }

            return new SpawnEntityAction(entityType, customName, location, amount);
        };
    }
}
