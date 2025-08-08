package com.vanixmc.events.action.domain;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.domain.TriggerHolder;

public abstract class AbstractAction implements Action {
    private final TriggerHolder triggerHolder;

    protected AbstractAction() {
        triggerHolder = new TriggerHolder();
    }

    @Override
    public boolean trigger(Context context) {
        return execute(context);
    }

    @Override
    public TriggerHolder getTriggerHolder() {
        return triggerHolder;
    }
}
