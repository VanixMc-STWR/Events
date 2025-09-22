package com.vanixmc.events.action.core_actions.entity_action;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.EntityUtils;
import com.vanixmc.events.util.RegionUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import java.util.Optional;

@Getter
public class SpawnEntityAction extends AbstractAction {
    private final EntityType entityType;
    private final Location location;
    @Nullable
    private final String name;
    private final int amount;

    public SpawnEntityAction(EntityType entityType, Location location, @Nullable String name, int amount) {
        this.entityType = entityType;
        this.location = location;
        this.name = name;
        this.amount = amount;
    }

    //TODO: Constructor for MythicMobs Id here>>>

    @Override
    public boolean execute(Context context) {
        for (int i = 0; i < amount; i++) {
            Entity entity = location.getWorld().spawn(location, entityType.getEntityClass());
            if (name != null) entity.setCustomName(name);
        }
        return true;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String worldName = config.getString("world-name");
            if (worldName.isEmpty()) {
                throw new IllegalArgumentException("world-name cannot be null or empty.");
            }

            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new IllegalArgumentException(
                        String.format("Invalid world-name value; no corresponding world for %s.", worldName));
            }

            String regionId = config.getString("region-id");
            if (regionId.isEmpty()) {
                throw new IllegalArgumentException("region-id cannot be null or empty.");
            }

            Optional<ProtectedRegion> region = RegionUtils.getRegionById(world, regionId);
            if (region.isEmpty()) {
                throw new IllegalArgumentException("Invalid region-id value; no corresponding region.");
            }

            Location location = RegionUtils.getLocationByRegion(world, region.get());
            //  to ensure that entity spawns at surface level.
            int y = world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
            location.setY(y + 1);

            String entityTypeString = config.getString("entity-type");

            if (!EntityUtils.isValidEntityType(entityTypeString.toUpperCase())) {
                throw new IllegalArgumentException("Invalid entity type.");
            }

            EntityType entityType = EntityType.valueOf(entityTypeString
                    .toUpperCase().trim());

            String name = config.getString("name");

            int amount = config.getInt("amount");

            return new SpawnEntityAction(entityType, location, name, amount);
        };
    }
}
