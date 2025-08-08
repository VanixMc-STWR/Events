package com.vanixmc.events.action.domain;

import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface Action extends Triggerable {
    boolean execute(EventContext context);

    TriggerHolder getTriggerHolder();
}