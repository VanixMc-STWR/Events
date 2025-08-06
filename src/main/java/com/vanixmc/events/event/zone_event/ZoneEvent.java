package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import lombok.Getter;

import java.util.List;

@Getter
public class ZoneEvent extends AbstractEvent {
    private final String regionId;

    public ZoneEvent(String id, String regionId, ConditionHolder conditionHolder, ActionHolder actionHolder) {
        super(id, new TriggerHolder(), conditionHolder, actionHolder);
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

    public static ConfigBuilder<Event> builder() {
        return config -> {
            String id = config.getString("id");
            String regionId = config.getString("region-id");
            List<Object> triggers = config.getObjectList("triggers");
            List<Object> conditions = config.getObjectList("conditions");
            List<Object> actions = config.getObjectList("actions");

            EventsPlugin instance = EventsPlugin.getInstance();
            ZoneEvent zoneEvent = new ZoneEvent(id, regionId, instance.getConditionFactory().createConditionHolder(conditions),
                    instance.getActionFactory().createActionHolder(actions));
            zoneEvent.getTriggerHolder().populate(instance.getTriggerFactory().createTriggerHolder(triggers, zoneEvent));
            return zoneEvent;
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
