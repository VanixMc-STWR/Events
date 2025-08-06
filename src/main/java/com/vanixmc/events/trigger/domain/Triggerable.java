package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.event.domain.EventContext;

public interface Triggerable {
    void trigger(EventContext context);
}
