package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface Event extends Triggerable {
    String getId();

    ConditionHolder getConditionHolder();

    ActionHolder getActionHolder();

    ActionHolder getStartActionHolder();

    boolean start();

    boolean stop();

    boolean execute(EventContext context);

    boolean isRunning();
}