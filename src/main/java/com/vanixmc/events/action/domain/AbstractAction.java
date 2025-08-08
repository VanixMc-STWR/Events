package com.vanixmc.events.action.domain;

import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.trigger.domain.TriggerHolder;

public abstract class AbstractAction implements Action {
    private final TriggerHolder triggerHolder;

    protected AbstractAction() {
        triggerHolder = new TriggerHolder();
    }

    @Override
    public boolean trigger(EventContext context) {
        return execute(context);
    }

    @Override
    public TriggerHolder getTriggerHolder() {
        return triggerHolder;
    }
}
