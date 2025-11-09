package com.vanixmc.events.action.particle_action;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import org.bukkit.*;
import javax.annotation.Nullable;

public class SpawnParticleAction extends AbstractAction {

    private final World world;
    private final Location location;
    private final Particle particle;
    private final int amount;
    @Nullable
    private final Particle.DustOptions dustOptions;

    public SpawnParticleAction(World world, Location location, Particle particle,
                               int amount, @Nullable Particle.DustOptions dustOptions) {
        this.world = world;
        this.location = location;
        this.particle = particle;
        this.amount = amount;
        this.dustOptions = dustOptions;
    }

    @Override
    public boolean execute(Context context) {
        if (dustOptions != null) {
            world.spawnParticle(particle, location, amount, dustOptions);
        } else {
            world.spawnParticle(particle, location, amount);
        }
        return true;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String worldName = config.getString("world");
            if (worldName == null) {
                throw new IllegalArgumentException("world_name cannot be empty.");
            }

            World world = Bukkit.getWorld(worldName);

            if (world == null) {
                throw new IllegalArgumentException("world does not exist.");
            }

            int x = config.getInt("x");
            int y = config.getInt("y");
            int z = config.getInt("z");

            Location location = new Location(world, x, y, z);

            int amount = config.getInt("amount");

            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid particle amount; must be greater than 0.");
            }

            Particle particle = Particle.valueOf(config.getString("particle"));

            if (particle.name().equals("DUST")) {
                return new SpawnParticleAction(world, location,
                        particle, amount, configureDustOptions(config));
            }

            if (particle.name().equals("DUST_COLOR_TRANSITION")) {
                return new SpawnParticleAction(world, location,
                        particle, amount, configureDustTransition(config));

            }

            return new SpawnParticleAction(world, location, particle, amount, null);
        };
    }

    private static Particle.DustOptions configureDustOptions(DomainConfig config) {
        String rgb = config.getString("color");

        int size = config.getInt("size");

        if (size <= 0) {
            throw new IllegalArgumentException("must use a particle size greater than zero.");
        }

        Color color = stringToRGB(rgb);

        return new Particle.DustOptions(color, size);
    }

    private static Particle.DustTransition configureDustTransition(DomainConfig config) {
        String rgbTo = config.getString("to_color");

        String rgbFrom = config.getString("from_color");

        int size = config.getInt("size");

        if (size <= 0) {
            throw new IllegalArgumentException("must use a particle size greater than zero.");
        }

        Color toColor = stringToRGB(rgbTo);

        Color fromColor = stringToRGB(rgbFrom);

        return new Particle.DustTransition(fromColor, toColor, size);
    }

    private static Color stringToRGB(String rgb) {
        String[] rgbValues = rgb.split("[ ,]+");

        int[] rgbInts = new int[3];

        for (int i = 0; i < rgbValues.length; i++) {
            rgbInts[i] = Integer.valueOf(rgbValues[i]);
        }

        for (int i : rgbInts) {
            if (i > 255 || i < 0) {
                throw new IllegalArgumentException("must use RGB values between 0 and 255 inclusive.");
            }
        }

        Color color = Color.fromRGB(rgbInts[0], rgbInts[1], rgbInts[2]);

        return color;
    }
}
