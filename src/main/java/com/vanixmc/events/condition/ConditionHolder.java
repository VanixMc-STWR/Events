package com.vanixmc.events.condition;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class ConditionHolder {
    private final List<EventCondition> conditions;

    public ConditionHolder(EventCondition... conditions) {
        this.conditions = new ArrayList<>(List.of(conditions));
    }
}
