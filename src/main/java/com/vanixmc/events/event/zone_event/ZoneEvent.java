package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.action.factory.ActionFactory;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;

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

    public static ConfigBuilder<Event> build(String id, ActionFactory actionFactory, ConditionFactory conditionFactory) {
        return config -> {
            String regionId = config.getString("region-id");
            List<Object> conditions = config.getObjectList("conditions");
            List<Object> actions = config.getObjectList("actions");

            return new ZoneEvent(id, regionId, conditionFactory.createConditionHolder(conditions),
                    actionFactory.createActionHolder(actions));
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
