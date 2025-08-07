package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public abstract class AbstractEvent implements Event {
    private final String id;
    private final ConditionHolder conditionHolder;
    private final ActionHolder startActionHolder;
    private final ActionHolder actionHolder;

    @Setter
    private boolean running;

    public AbstractEvent(String id) {
        this.id = id;
        this.startActionHolder = new ActionHolder();
        this.conditionHolder = new ConditionHolder();
        this.actionHolder = new ActionHolder();
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
    public ActionHolder getStartActionHolder() {
        return startActionHolder;
    }

    @Override
    public boolean execute(EventContext context) {
        if (!conditionHolder.checkAll(context)) return false;
        actionHolder.executeAll(context);
        return true;
    }

    @Override
    public boolean trigger(EventContext context) {
        return execute(context);
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
