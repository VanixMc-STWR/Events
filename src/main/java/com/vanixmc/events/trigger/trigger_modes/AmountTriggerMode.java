package com.vanixmc.events.trigger.trigger_modes;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.Triggerable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AmountTriggerMode implements TriggerMode {
    private final int maxAmount;
    private int timesTriggered;

    public AmountTriggerMode(int maxAmount, int timesTriggered) {
        this.timesTriggered = timesTriggered;
        this.maxAmount = maxAmount;
    }

    @Override
    public void evaluate(Trigger trigger, Triggerable triggerable) {
        if (maxAmount == -1) return;

        // Increment triggered amount
        timesTriggered++;

        if (!(timesTriggered >= maxAmount)) return;
        trigger.unsubscribe(triggerable);
    }

    public static ConfigBuilder<TriggerMode> builder() {
        return config -> {
            Integer maxAmount = config.getInt("amount");
            Integer timesTriggered = config.getInt("times-triggered");

            if (maxAmount == null) {
                throw new IllegalArgumentException("MaxAmount cannot be null for multi-trigger-mode!");
            }

            return new AmountTriggerMode(maxAmount, timesTriggered != null ? timesTriggered : 1);
        };
    }
}

