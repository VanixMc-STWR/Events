package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface Event extends Triggerable {
    String getId();

    ConditionHolder getConditionHolder();

    ActionHolder getActionHolder();

    boolean start();

    boolean stop();

    void execute(EventContext context);

    boolean isRunning();
}