package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.context.Context;

public interface Triggerable {
    boolean trigger(Context context);
}
