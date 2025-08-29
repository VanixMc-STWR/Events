package com.vanixmc.events.action.domain;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface Action extends Triggerable {
    boolean execute(Context context);

    TriggerHolder getTriggerHolder();
}