package com.vanixmc.events.trigger.trigger_modes.factory;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import com.vanixmc.events.trigger.trigger_modes.TriggerModeType;
import com.vanixmc.events.trigger.trigger_modes.multi.MultiTriggerMode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TriggerModeFactory {
    private final Map<TriggerModeType, ConfigBuilder<TriggerMode>> builders;

    @Getter
    private final HashMap<String, TriggerMode> registry;

    public TriggerModeFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
        registerAllTriggerTypes();
    }

    public void registerBuilder(TriggerModeType type, ConfigBuilder<TriggerMode> builder) {
        builders.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> triggers) {
        for (String key : triggers.keySet()) {
            TriggerMode trigger = create(key, triggers);
            registry.put(key, trigger);
        }
    }

    public TriggerMode create(String key, Map<String, Map<String, Object>> triggers) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, triggers);
        TriggerModeType type = TriggerModeType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<TriggerMode> builder = builders.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown trigger mode type: " + type);
        }
        return builder.build(config);
    }

    public void registerAllTriggerTypes() {
        builders.put(TriggerModeType.MULTI, MultiTriggerMode.builder());
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
