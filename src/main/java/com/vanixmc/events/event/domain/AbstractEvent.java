package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractEvent implements Event {
    private final String id;
    private final TriggerHolder triggerHolder;
    private final ConditionHolder conditionHolder;
    private final ActionHolder actionHolder;

    @Setter
    private boolean running;

    public AbstractEvent(String id, TriggerHolder triggerHolder, ConditionHolder conditionHolder, ActionHolder actionHolder) {
        this.id = id;
        this.triggerHolder = triggerHolder;
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
    public void execute(EventContext context) {
        if (!conditionHolder.checkAll(context)) return;
        actionHolder.executeAll(context);
    }

    @Override
    public void trigger(EventContext context) {
        execute(context);
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
