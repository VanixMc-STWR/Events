package com.vanixmc.events.action.impl.highlight_region_action;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shapes.CuboidWithTitle;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import me.ogali.rendered.api.Rendered;
import me.ogali.rendered.renderer.display.RenderSettings;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Optional;

@AllArgsConstructor
@Getter
@ToString
public class RegionHighlightAction extends AbstractAction {
    private final String worldName;
    private final String regionName;

    @Override
    public boolean execute(Context context) {
        Optional<ProtectedRegion> regionById = RegionUtils.getRegionById(worldName, regionName);
        regionById.ifPresent(region -> {
            BlockVector3 minimumPoint = region.getMinimumPoint();
            BlockVector3 maximumPoint = region.getMaximumPoint();
            Location cornerOne = new Location(Bukkit.getWorld(worldName), minimumPoint.x(), minimumPoint.y(), minimumPoint.z());
            Location cornerTwo = new Location(Bukkit.getWorld(worldName), maximumPoint.x(), maximumPoint.y(), maximumPoint.z());
            Rendered.getInstance().renderShape(regionName, new CuboidWithTitle(cornerOne, cornerTwo, 0.4f),
                    RenderSettings.builder()
                            .lineThickness(0.04f)
                            .lineMaterial(Material.WHITE_CONCRETE)
                            .lineColor(Color.ORANGE).glowing(true)
                            .persistent(false)
                            .build());
        });
        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String worldName = config.getString("world-name");
            if (worldName.isEmpty()) {
                throw new IllegalArgumentException("world-name cannot be null or empty.");
            }

            String regionName = config.getString("region-name");
            if (regionName.isEmpty()) {
                throw new IllegalArgumentException("region-name cannot be null or empty.");
            }


            return new RegionHighlightAction(worldName, regionName);
        };
    }
}
