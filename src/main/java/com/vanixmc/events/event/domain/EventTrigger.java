package com.vanixmc.events.event.domain;

import com.vanixmc.events.condition.ConditionHolder;

public class EventTrigger {
    private final String id;
    private final ConditionHolder conditionHolder;

    public EventTrigger(String id, ConditionHolder conditionHolder) {
        this.id = id;
        this.conditionHolder = new ConditionHolder();
    }
}
