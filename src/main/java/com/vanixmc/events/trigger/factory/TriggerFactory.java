package com.vanixmc.events.trigger.factory;

import com.vanixmc.events.shared.AbstractFactory;
import com.vanixmc.events.shared.BuilderKey;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.domain.Trigger;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.domain.Triggerable;
import com.vanixmc.events.trigger.non_listener_triggers.RecurringTrigger;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import com.vanixmc.events.trigger.trigger_modes.factory.TriggerModeFactory;
import com.vanixmc.events.trigger.triggers.listener_triggers.EntityKilledTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.PlayerInteractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.region_trigger.RegionInteractTrigger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class TriggerFactory extends AbstractFactory<AbstractTrigger, Trigger> {

    @Override
    public void registerAllBuilders() {
        registerBuilder(BuilderKey.of("region_interact", "region_int", "rg_int"), RegionInteractTrigger.builder());
        registerBuilder(BuilderKey.of("player_interact", "p_interact"), PlayerInteractTrigger.builder());
        registerBuilder(BuilderKey.of("entity_kill_entity", "e_kill_e"), EntityKilledTrigger.builder());
        registerBuilder(BuilderKey.of("recurring"), RecurringTrigger.builder());
    }

    @Override
    public AbstractTrigger create(String key, Map<String, Map<String, Object>> fileData) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, fileData);
        String type = config.getString("type");
        ConfigBuilder<AbstractTrigger> builder = this.getBuilder(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }
        Object triggerModeFromConfig = config.getObject("mode");
        TriggerMode triggerMode;

        TriggerModeFactory triggerModeFactory = TriggerModeFactory.getInstance();
        if (triggerModeFromConfig == null) {
            ConfigBuilder<TriggerMode> triggerModeBuilder = triggerModeFactory
                    .getBuilder("inf");
            triggerMode = triggerModeBuilder.build(new DomainConfig());
        } else {
            triggerMode = triggerModeFactory.getTriggerMode(triggerModeFromConfig);
        }
        AbstractTrigger trigger = builder.build(config);
        trigger.setTriggerMode(triggerMode);
        trigger.setId(config.getId());
        return trigger;
    }

    public TriggerHolder createTriggerHolder(List<Object> triggers, Triggerable triggerable) {
        List<Trigger> resolvedTriggers = new ArrayList<>();

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
                    resolvedTriggers.add(reusable);
                } else {
                    // Inline action
                    String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, triggerData);
                    Trigger inlineTrigger = create(tempKey, wrapper);
                    inlineTrigger.subscribe(triggerable);
                    resolvedTriggers.add(inlineTrigger);
                }
            } else {
                throw new IllegalArgumentException("Trigger entry must be a map: " + item);
            }
        }
        return new TriggerHolder(resolvedTriggers);
    }

    //#region Lazy Initialization
    public static TriggerFactory getInstance() {
        return TriggerFactory.InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        private static final TriggerFactory instance = new TriggerFactory();
    }
    //#endregion
}
