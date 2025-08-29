package com.vanixmc.events.trigger.trigger_modes.factory;

import com.vanixmc.events.shared.AbstractFactory;
import com.vanixmc.events.shared.BuilderKey;
import com.vanixmc.events.trigger.trigger_modes.AmountTriggerMode;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class TriggerModeFactory extends AbstractFactory<TriggerMode, TriggerMode> {

    @Override
    public void registerAllBuilders() {
        registerBuilder(BuilderKey.of("infinite", "inf"), config -> new AmountTriggerMode(-1, 0));
        registerBuilder(BuilderKey.of("once", "one", "1"), config -> new AmountTriggerMode(1, 0));
        registerBuilder(BuilderKey.of("multiple", "multi"), AmountTriggerMode.builder());
    }

    public TriggerMode getTriggerMode(Object triggerMode) {
        if (triggerMode instanceof Map<?, ?> map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> triggerData = (Map<String, Object>) map;

            // Reference by id
            if (triggerData.containsKey("id") && triggerData.size() == 1) {
                String id = (String) triggerData.get("id");
                TriggerMode reusable = registry.get(id);
                if (reusable == null) {
                    throw new IllegalArgumentException("Unknown reusable trigger mode: " + id);
                }
                return reusable;
            } else {
                // Inline action
                String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
                Map<String, Map<String, Object>> wrapper = Map.of(tempKey, triggerData);
                return create(tempKey, wrapper);
            }
        } else {
            throw new IllegalArgumentException("TriggerMode entry must be a map: " + triggerMode);
        }
    }

    //#region Lazy Initialization
    public static TriggerModeFactory getInstance() {
        return TriggerModeFactory.InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        private static final TriggerModeFactory instance = new TriggerModeFactory();
    }
    //#endregion
}