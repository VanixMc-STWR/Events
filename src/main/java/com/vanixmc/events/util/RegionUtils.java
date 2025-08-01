package com.vanixmc.events.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class RegionUtils {

    public static ProtectedRegion getRegionById(String worldName, String regionId) {
        //  convert bukkit world to worldguard world
        //  to use region features of API.
        World bukkitWorld = Bukkit.getWorld(worldName);
        if (bukkitWorld == null) return null;

        com.sk89q.worldedit.world.World wgWorld = BukkitAdapter.adapt(bukkitWorld);

        //  Acquire region manager for given world and return region
        //  for given ID.
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(wgWorld);
        if (regionManager == null) return null;

        return regionManager.getRegion(regionId);
    }

    public static boolean isLocationInRegion(Location location, Region region) {
        //  adapt location to WorldEdit 3D coordinate.
        BlockVector3 wgLocationPos = BukkitAdapter.asBlockVector(location);

        if (wgLocationPos == null) return false;

        return region.contains(wgLocationPos);
    }

    public static boolean isLocationInRegion(String worldName, Location location, String regionId) {

        ProtectedRegion region = getRegionById(worldName, regionId);

        if (region == null) return false;

        //  adapt location to WorldEdit 3D coordinate.
        BlockVector3 wgLocationPos = BukkitAdapter.asBlockVector(location);

        if (wgLocationPos == null) return false;

        return region.contains(wgLocationPos);
    }
}
