package com.vanixmc.events.trigger.factory;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.TriggerType;
import com.vanixmc.events.trigger.domain.Triggerable;
import com.vanixmc.events.trigger.listener_triggers.RegionEnterTrigger;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TriggerFactory {
    private final Map<TriggerType, ConfigBuilder<Trigger>> builders;

    @Getter
    private final HashMap<String, Trigger> registry;

    public TriggerFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
        registerAllTriggerTypes();
    }

    public void registerBuilder(TriggerType type, ConfigBuilder<Trigger> builder) {
        builders.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> triggers) {
        for (String key : triggers.keySet()) {
            Trigger trigger = create(key, triggers);
            registry.put(key, trigger);
        }
    }

    public Trigger create(String key, Map<String, Map<String, Object>> triggers) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, triggers);
        TriggerType type = TriggerType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<Trigger> builder = builders.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown trigger type: " + type);
        }
        return builder.build(config);
    }

    public void registerAllTriggerTypes() {
        builders.put(TriggerType.REGION_ENTER, RegionEnterTrigger.builder());
    }

    public void subscribeTriggers(List<Object> triggers, Triggerable triggerable) {
        if (triggers == null) return;
        for (Object item : triggers) {
            if (item instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> triggerData = (Map<String, Object>) map;

                // Reference by id
                if (triggerData.containsKey("id") && triggerData.size() == 1) {
                    String id = (String) triggerData.get("id");
                    Trigger reusable = registry.get(id);
                    if (reusable == null) {
                        throw new IllegalArgumentException("Unknown reusable trigger: " + id);
                    }
                    reusable.subscribe(triggerable);
                } else {
                    // Inline action
                    String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, triggerData);
                    Trigger inlineTrigger = create(tempKey, wrapper);
                    inlineTrigger.subscribe(triggerable);
                }
            } else {
                throw new IllegalArgumentException("Trigger entry must be a map: " + item);
            }
        }
    }

    //#region Lazy Initialization
    public static TriggerFactory getInstance() {
        return InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        private static final TriggerFactory instance = new TriggerFactory();
    }
    //#endregion
}
