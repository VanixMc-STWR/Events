package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.RegionUtils;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ZoneEvent extends AbstractEvent {
    private final String regionId;

    public ZoneEvent(String id, String regionId, ConditionHolder conditionHolder, ActionHolder actionHolder) {
        super(id, conditionHolder, actionHolder);
        this.regionId = regionId;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public void execute(EventContext eventContext) {
        Player player = eventContext.player();
        if (player == null) return;
        if (!(RegionUtils.isLocationInRegion(player.getLocation(), regionId))) return;
        super.execute(eventContext);
    }

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            String regionId = config.getString("region-id");
            List<Object> conditions = config.getObjectList("conditions");
            List<Object> actions = config.getObjectList("actions");

            EventsPlugin instance = EventsPlugin.getInstance();
            return new ZoneEvent(id, regionId, instance.getConditionFactory().createConditionHolder(conditions),
                    instance.getActionFactory().createActionHolder(actions));
        };
    }

    @Override
    public String toString() {
        return "ZoneEvent{" +
                "id='" + getId() + '\'' +
                ", regionId='" + regionId + '\'' +
                ", running=" + isRunning() +
                ", conditionHolder=" + getConditionHolder() +
                ", actionHolder=" + getActionHolder() +
                '}';
    }
}
