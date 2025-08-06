package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;

public interface Event {
    String getId();

    ConditionHolder getConditionHolder();

    ActionHolder getActionHolder();

    boolean start();

    boolean stop();

    void execute(EventContext eventContext);

    boolean isRunning();
}