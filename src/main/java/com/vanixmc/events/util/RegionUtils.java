package com.vanixmc.events.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;
import java.util.Optional;

public class RegionUtils {

    public static Optional<ProtectedRegion> getRegionById(String worldName, String regionId) {
        return Optional.ofNullable(Bukkit.getWorld(worldName))
                //  use BukkitAdapter to convert Bukkit world object to
                //  WorldGuard object.
                .map(BukkitAdapter::adapt)
                //  Get RegionManager via instance of RegionContainer from
                //  WorldGuard world.
                .map(wgWorld -> WorldGuard.getInstance()
                        .getPlatform().getRegionContainer().get(wgWorld))
                //  return region via regionId.
                .map(regionManager -> regionManager.getRegion(regionId));
    }

    public static Optional<ProtectedRegion> getRegionById(World world, String regionId) {
        return Optional.ofNullable(world)
                .map(BukkitAdapter::adapt)
                .map(wgWorld -> WorldGuard.getInstance()
                        .getPlatform().getRegionContainer().get(wgWorld))
                //  return region via regionId.
                .map(regionManager -> regionManager.getRegion(regionId));
    }

    public static boolean isLocationInRegion(Location location, String regionId) {
        return getRegionById(Objects.requireNonNull(location.getWorld()),
                regionId)
                //  Given the ProtectedRegion determine if the
                //  corresponding block vector to the location exists
                //  in region.
                .map(region ->
                        region.contains(BukkitAdapter.asBlockVector(location)))
                .orElse(false);
    }

    public static boolean isLocationInRegion(Location location, ProtectedRegion region) {
        return Optional.ofNullable(location)
                .map(BukkitAdapter::asBlockVector)
                .filter(pos -> region != null)
                .map(region::contains)
                .orElse(false);
    }

    public static ApplicableRegionSet getRegionsByLocation(Location location) {
        World bukkitWorld = location.getWorld();
        if (bukkitWorld == null) return null;

        var wgWorld = BukkitAdapter.adapt(bukkitWorld);
        return WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .get(wgWorld)
                .getApplicableRegions(BlockVector3.at(
                        location.getX(),
                        location.getY(),
                        location.getZ()
                ));
    }

    public static Location getLocationByRegion(World world, ProtectedRegion region) {

        BlockVector3 min = region.getMinimumPoint();
        BlockVector3 max = region.getMaximumPoint();

        Bukkit.getLogger().info("Min: " + min + ", Max: " + max);

        //  acquires the location's center, float values are
        //  for offsetting from corner to center of block.

        double centerX = (min.x() + max.x()) / 2.0;
        double centerY = (min.y() + max.y()) / 2.0;
        double centerZ = (min.z() + max.z()) / 2.0;

        return new Location(world, centerX, centerY, centerZ);
    }
}
