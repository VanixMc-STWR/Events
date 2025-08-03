package com.vanixmc.events.condition.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class ConditionHolder {
    private final List<Condition> conditions;

    public ConditionHolder(Condition... conditions) {
        this.conditions = new ArrayList<>(List.of(conditions));
    }

    public ConditionHolder(List<Condition> conditions) {
        this.conditions = new ArrayList<>(conditions);
    }

}

