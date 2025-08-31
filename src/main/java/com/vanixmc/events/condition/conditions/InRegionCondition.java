package com.vanixmc.events.condition.conditions;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

@Getter
@ToString
@AllArgsConstructor
public class InRegionCondition implements Condition {
    private final String regionId;
    private final boolean value;

    @Override
    public boolean test(Context context) {
        Location locationFromContext = getLocationFromContext(context);
        if (locationFromContext == null) return false;

        return RegionUtils.isLocationInRegion(locationFromContext, regionId) == value;
    }

    public static ConfigBuilder<Condition> builder() {
        return config -> {
            String regionId = config.getString("region-id");
            if (regionId.isEmpty()) {
                throw new IllegalArgumentException("Region id cannot be null or empty.");
            }
            Boolean value = config.getBoolean("value");
            if (value == null) {
                value = true;
            }

            return new InRegionCondition(regionId, value);
        };
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
