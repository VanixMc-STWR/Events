package com.vanixmc.events.condition.domain;

import com.vanixmc.events.context.Context;

@FunctionalInterface
public interface Condition {
    boolean test(Context context);

    default Condition and(Condition other) {
        return context -> this.test(context) && other.test(context);
    }

    default Condition or(Condition other) {
        return context -> this.test(context) || other.test(context);
    }
}


