package com.vanixmc.events.trigger.trigger_modes;

import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.Triggerable;

public interface TriggerMode {
    void evaluate(Trigger trigger, Triggerable triggerable);
}
