package com.vanixmc.events.action.particle_action;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public class SpawnParticleAction extends AbstractAction {
    private final Location location;

    public SpawnParticleAction(Location location) {
        this.location = location;
    }

    @Override
    public boolean execute(Context context) {
        return true;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String worldName = config.getString("world");
            if (worldName == null) {
                throw new IllegalArgumentException("world cannot be null or empty.");
            }

            World world = Bukkit.getWorld(worldName);

            String regionId = config.getString("region-id");
            if (regionId == null) {
                throw new IllegalArgumentException("region-id cannot be null or empty.");
            }

            Optional<ProtectedRegion> region = RegionUtils.getRegionById(world, regionId);

            if (region.isEmpty()) {
                throw new IllegalArgumentException("Invalid region-id value; no corresponding region.");
            }

            return new SpawnParticleAction(location);
        };
    }
}
