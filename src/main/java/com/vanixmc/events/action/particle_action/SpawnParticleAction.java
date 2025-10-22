package com.vanixmc.events.action.particle_action;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Optional;

public class SpawnParticleAction extends AbstractAction {

    private final World world;
    private final Location location;
    private final Particle particle;
    private final int amount;

    public SpawnParticleAction(World world, Location location, Particle particle, int amount) {
        this.world = world;
        this.location = location;
        this.particle = particle;
        this.amount = amount;
    }

    @Override
    public boolean execute(Context context) {
        world.spawnParticle(particle, location, amount);
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

            Location location = RegionUtils.getLocationByRegion(world, region.get());
            int y = world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
            location.setY(y+2);

            String particleName = config.getString("particle");

            Optional<Particle> particle = Optional.of(Particle.valueOf(particleName));

            if (particle.isEmpty()) {
                throw new IllegalArgumentException("Invalid particle argument; must be a valid particle type.");
            }

            int amount = config.getInt("amount");

            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid particle amount; must be greater than 0.");
            }

            return new SpawnParticleAction(world, location, particle.get(), amount);
        };
    }
}
