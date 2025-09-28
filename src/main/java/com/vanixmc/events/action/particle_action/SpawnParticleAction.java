package com.vanixmc.events.action.particle_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import hm.zelha.particlesfx.shapers.parents.Shape;
import org.bukkit.Location;

public class SpawnParticleAction extends AbstractAction {
    private final Location location;
    private final Shape shape;

    public SpawnParticleAction(Location location, Shape shape) {
        this.location = location;
        this.shape = shape;
    }

    @Override
    public boolean execute(Context context) {
        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {

    }
}
