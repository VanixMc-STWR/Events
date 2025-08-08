package com.vanixmc.events.trigger.factory;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.domain.TriggerType;
import com.vanixmc.events.trigger.domain.Triggerable;
import com.vanixmc.events.trigger.listener_triggers.RegionEnterTrigger;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import com.vanixmc.events.trigger.trigger_modes.TriggerModeType;
import com.vanixmc.events.trigger.trigger_modes.factory.TriggerModeFactory;
import lombok.Getter;

import java.util.*;

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

        Object triggerModeObject = config.getObject("mode");
        TriggerMode triggerMode = TriggerModeFactory.getInstance()
                .getTriggerMode(triggerModeObject);


        if (triggerModeObject == null) {
            triggerMode = TriggerModeFactory.getInstance()
                    .getBuilders()
                    .get(TriggerModeType.INFINITE)
                    .build(new DomainConfig());
        }
        config.getConfig().put("trigger-mode", triggerMode);

        return builder.build(config);
    }

    public void registerAllTriggerTypes() {
        builders.put(TriggerType.REGION_ENTER, RegionEnterTrigger.builder());
    }

    public TriggerHolder createTriggerHolder(List<Object> triggers, Triggerable triggerable) {
        List<Trigger> resolvedTriggers = new ArrayList<>();

        for (Object item : triggers) {
            if (item instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> actionData = (Map<String, Object>) map;

                // Reference by id
                if (actionData.containsKey("id") && actionData.size() == 1) {
                    String id = (String) actionData.get("id");
                    Trigger reusable = registry.get(id);
                    if (reusable == null) {
                        throw new IllegalArgumentException("Unknown reusable action: " + id);
                    }
                    resolvedTriggers.add(reusable);
                    reusable.subscribe(triggerable);
                } else {
                    // Inline action
                    String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, actionData);
                    Trigger inlineTrigger = create(tempKey, wrapper);
                    resolvedTriggers.add(inlineTrigger);
                    inlineTrigger.subscribe(triggerable);
                }
            } else {
                throw new IllegalArgumentException("Action entry must be a map: " + item);
            }
        }
        return new TriggerHolder(resolvedTriggers);
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
