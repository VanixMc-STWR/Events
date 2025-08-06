package com.vanixmc.events.trigger.trigger_modes;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.Triggerable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MultiTriggerMode implements TriggerMode {
    private final int maxAmount;
    private int currentAmount;

    public MultiTriggerMode(int currentAmount, int maxAmount) {
        this.maxAmount = maxAmount;
        this.currentAmount = currentAmount;
    }

    @Override
    public void evaluate(Trigger trigger, List<Triggerable> subscribers) {
        if (currentAmount++ >= maxAmount) {
            List<Triggerable> subscribersClone = new ArrayList<>(subscribers);
            subscribersClone.forEach(trigger::unsubscribe);
        }
    }

    public static ConfigBuilder<TriggerMode> builder() {
        return config -> {
            Integer maxAmount = config.getInt("max-amount");
            Integer currentAmount = config.getInt("current-amount");

            if (maxAmount == null) {
                throw new IllegalArgumentException("MaxAmount cannot be null!");
            }

            if (currentAmount == null) {
                currentAmount = 0;
            }

            return new MultiTriggerMode(currentAmount, maxAmount);
        };
    }
}

