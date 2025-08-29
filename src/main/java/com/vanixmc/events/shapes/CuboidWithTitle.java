package com.vanixmc.events.shapes;

import me.ogali.rendered.renderer.display.DisplayInfo;
import me.ogali.rendered.renderer.shapes.Cuboid;
import org.bukkit.Location;

import java.util.List;

public class CuboidWithTitle extends Cuboid {
    private final String title;

    public CuboidWithTitle(Location cornerOne, Location cornerTwo, float lineThickness, String title) {
        super(cornerOne, cornerTwo, lineThickness);
        this.title = title;
    }

    @Override
    public List<DisplayInfo> getDisplays() {
        return title.isEmpty() ? List.of() : List.of(
                DisplayInfo.text(getCenter().clone().add(0.5, 3.5, 0.5), title)
        );
    }
}