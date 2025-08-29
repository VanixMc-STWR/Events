package com.vanixmc.events.condition.domain;

import com.vanixmc.events.context.Context;
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

    public void populate(ConditionHolder conditionHolder) {
        this.conditions.addAll(conditionHolder.getConditions());
    }

    public boolean checkAllAndExecuteActions(Context context) {
        for (Condition condition : conditions) {
            if (!condition.test(context)) return false;
        }
        return true;
    }
}

