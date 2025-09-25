package com.vanixmc.events.action.core_actions.entity_action;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.EntityUtils;
import com.vanixmc.events.util.RegionUtils;
import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.core.mobs.ActiveMob;
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

    private enum MobType {MYTHIC, BUKKIT}

    private final MobType mobType;
    private final EntityType entityType;
    private final MythicMob mythicMob;
    private final Location location;
    @Nullable
    private final String name;
    private final int amount;

    public SpawnEntityAction(EntityType entityType, Location location, @Nullable String name, int amount) {
        this.mobType = MobType.BUKKIT;
        this.entityType = entityType;
        this.mythicMob = null;
        this.location = location;
        this.name = name;
        this.amount = amount;
    }

    public SpawnEntityAction(MythicMob mythicMob, Location location, @Nullable String name, int amount) {
        this.mobType = MobType.MYTHIC;
        this.mythicMob = mythicMob;
        this.entityType = null;
        this.location = location;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public boolean execute(Context context) {
        switch (mobType) {
            case MYTHIC:
                for (int i = 0; i < amount; i++) {
                    ActiveMob active = mythicMob.spawn(BukkitAdapter.adapt(location),1);
                    active.setDisplayName(name);
                }
            case BUKKIT:
                for (int i = 0; i < amount; i++) {
                    Entity spawnedEntity = location.getWorld().spawnEntity(location, entityType);
                    if (name != null) spawnedEntity.setCustomName(name);
                }
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

            String name = config.getString("name");

            int amount = config.getInt("amount");

            String entityId = config.getString("entity-type");

            //  check if id corresponds to MythicMob
            Optional<MythicMob> mythicMob = EntityUtils.getMythicMob(entityId);
            if (mythicMob.isPresent()) {
                return new SpawnEntityAction(mythicMob.get(), location, name, amount);
            }

            Optional<EntityType> entityType = EntityUtils.getVanillaEntityType(entityId);
            if (entityType.isEmpty()) {
                throw new IllegalArgumentException("Invalid entity id; no corresponding entity.");
            }

            return new SpawnEntityAction(entityType.get(), location, name, amount);
        };
    }
}
