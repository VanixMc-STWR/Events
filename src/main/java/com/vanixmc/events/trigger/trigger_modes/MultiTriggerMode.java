package com.vanixmc.events.trigger.trigger_modes;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.Triggerable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultiTriggerMode implements TriggerMode {
    private final int maxAmount;
    private int timesTriggered;

    public MultiTriggerMode(int maxAmount, int timesTriggered) {
        this.timesTriggered = timesTriggered;
        this.maxAmount = maxAmount;
    }

    @Override
    public void evaluate(Trigger trigger, Triggerable triggerable) {
        if (!(timesTriggered++ >= maxAmount)) return;
        trigger.unsubscribe(triggerable);
    }

    public static ConfigBuilder<TriggerMode> builder() {
        return config -> {
            Integer maxAmount = config.getInt("max-amount");
            Integer timesTriggered = config.getInt("times-triggered");

            if (maxAmount == null) {
                throw new IllegalArgumentException("MaxAmount cannot be null for multi-trigger-mode!");
            }

            return new MultiTriggerMode(maxAmount, timesTriggered != null ? timesTriggered : 0);
        };
    }
}

