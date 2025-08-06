package com.vanixmc.events.trigger.trigger_modes;

import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.Triggerable;

import java.util.List;

public interface TriggerMode {
    void evaluate(Trigger trigger, List<Triggerable> subscribers);
}
