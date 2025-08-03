package com.vanixmc.events.condition.domain;

import com.vanixmc.events.event.domain.EventContext;

@FunctionalInterface
public interface Condition {
    boolean test(EventContext eventContext);

    default Condition and(Condition other) {
        return context -> this.test(context) && other.test(context);
    }

    default Condition or(Condition other) {
        return context -> this.test(context) || other.test(context);
    }
}


