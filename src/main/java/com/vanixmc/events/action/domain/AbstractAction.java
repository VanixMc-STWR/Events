package com.vanixmc.events.action.domain;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAction implements Action {
    private final TriggerHolder triggerHolder;

    @Getter
    @Setter
    private @Nullable Event event;

    protected AbstractAction() {
        this.triggerHolder = new TriggerHolder();
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
