package com.vanixmc.events.condition;

import java.util.ArrayList;
import java.util.List;

public class ConditionHolder {
    private final List<EventCondition> conditions;

    public ConditionHolder(EventCondition... conditions) {
        this.conditions = new ArrayList<>(List.of(conditions));
    }
}
