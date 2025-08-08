package com.vanixmc.events.event.domain;

import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface Event extends Triggerable {
    String getId();

    ConditionHolder getConditionHolder();

    ActionHolder getActionHolder();

    TriggerHolder getTriggerHolder();

    boolean start();

    boolean stop();

    boolean execute(Context context);

    boolean isRunning();
}