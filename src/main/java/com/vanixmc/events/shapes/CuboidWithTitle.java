package com.vanixmc.events.shapes;

import me.ogali.rendered.renderer.display.DisplayInfo;
import me.ogali.rendered.renderer.shapes.Cuboid;
import org.bukkit.Location;

import java.util.List;

public class CuboidWithTitle extends Cuboid {

    public CuboidWithTitle(Location cornerOne, Location cornerTwo, float lineThickness) {
        super(cornerOne, cornerTwo, lineThickness);
    }

    @Override
    public List<DisplayInfo> getDisplays() {
        return List.of(
                DisplayInfo.text(getCenter().clone().add(0.5, 3.5, 0.5), "&6&lKOTH EVENT\n&7Enter region to participate!")
        );
    }
}
