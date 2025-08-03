package com.vanixmc.events.event.zone_event;

import com.vanixmc.events.action.ActionFactory;
import com.vanixmc.events.action.ActionHolder;
import com.vanixmc.events.condition.ConditionHolder;
import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.EventBuilder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
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

    public static EventBuilder build(String id, ActionFactory actionFactory) {
        return config -> {
            String regionId = config.getString("region-id");
            List<Object> actions = config.getObjectList("actions");

            return new ZoneEvent(id, regionId, new ConditionHolder(),
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
