package com.vanixmc.events.condition.conditions;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.EvaluatingCondition;
import com.vanixmc.events.condition.evaluator.Evaluator;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

@Getter
@ToString
public class InRegionCondition extends EvaluatingCondition {
    private final String regionId;

    protected InRegionCondition(Evaluator evaluator, String regionId) {
        super(evaluator);
        this.regionId = regionId;
    }

    @Override
    protected Object getExpected() {
        return true;
    }

    @Override
    protected Object getActual(Context context) {
        Location locationFromContext = getLocationFromContext(context);
        if (locationFromContext == null) return null;

        return RegionUtils.isLocationInRegion(locationFromContext, regionId);
    }

    public static ConfigBuilder<Condition> builder() {
        return buildWithEvaluator(((config, evaluator) -> {
            String regionId = config.getString("region-id");
            if (regionId.isEmpty()) {
                throw new IllegalArgumentException("Region id cannot be null or empty.");
            }

            return new InRegionCondition(evaluator, regionId);
        }));
    }

    private Location getLocationFromContext(Context context) {
        if (context.getLocation() != null) {
            return context.getLocation();
        }

        if (context.getEntity() != null) {
            return context.getEntity().getLocation();
        }

        if (context.getPlayer() != null) {
            return context.getPlayer().getLocation();
        }

        return null;
    }
}
