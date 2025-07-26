package com.vanixmc.events.event;

import com.vanixmc.events.action.ActionHolder;
import com.vanixmc.events.condition.ConditionHolder;

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

    public String getRegionId() {
        return regionId;
    }
}
