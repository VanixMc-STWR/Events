package com.vanixmc.events.event;

import com.vanixmc.events.action.ActionHolder;
import com.vanixmc.events.condition.ConditionHolder;

public abstract class AbstractEvent implements Event {
    private final String id;
    private final ConditionHolder conditionHolder;
    private final ActionHolder actionHolder;
    private boolean running;

    public AbstractEvent(String id, ConditionHolder conditionHolder, ActionHolder actionHolder) {
        this.id = id;
        this.conditionHolder = conditionHolder;
        this.actionHolder = actionHolder;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public ConditionHolder getConditionHolder() {
        return conditionHolder;
    }

    @Override
    public ActionHolder getActionHolder() {
        return actionHolder;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}
