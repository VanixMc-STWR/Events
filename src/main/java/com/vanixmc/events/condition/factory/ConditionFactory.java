package com.vanixmc.events.condition.factory;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.domain.ConditionType;
import com.vanixmc.events.condition.permission_condition.PermissionCondition;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import lombok.Getter;

import java.util.*;

public class ConditionFactory {
    private final Map<ConditionType, ConfigBuilder<Condition>> builders;

    @Getter
    private final HashMap<String, Condition> registry;

    public ConditionFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
    }

    public void registerBuilder(ConditionType type, ConfigBuilder<Condition> builder) {
        builders.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> conditions) {
        for (String key : conditions.keySet()) {
            Condition condition = create(key, conditions);
            registry.put(key, condition);
        }
    }

    public Condition create(String key, Map<String, Map<String, Object>> conditions) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, conditions);
        ConditionType type = ConditionType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<Condition> builder = builders.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }
        return builder.build(config);
    }

    public void registerAllConditionTypes() {
        builders.put(ConditionType.PERMISSION, PermissionCondition.builder());
    }

    public ConditionHolder createConditionHolder(List<Object> conditions) {
        List<Condition> resolvedConditions = new ArrayList<>();

        for (Object item : conditions) {
            if (item instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> conditionData = (Map<String, Object>) map;

                // Composite condition by ID string
                if (conditionData.containsKey("id") && conditionData.size() == 1) {
                    String id = (String) conditionData.get("id");

                    Condition resolved = parseCompositeId(id);
                    resolvedConditions.add(resolved);
                } else {
                    // Inline condition (not by id)
                    String tempKey = UUID.randomUUID().toString();
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, conditionData);
                    Condition inlineCondition = create(tempKey, wrapper);
                    resolvedConditions.add(inlineCondition);
                }
            } else {
                throw new IllegalArgumentException("Condition entry must be a map: " + item);
            }
        }

        return new ConditionHolder(resolvedConditions);
    }

    private Condition parseCompositeId(String id) {
        String[] andParts = id.split("\\s+and\\s+");

        if (andParts.length > 1) {
            return Arrays.stream(andParts)
                    .map(this::parseOrComposite)
                    .reduce(Condition::and)
                    .orElseThrow();
        }

        return parseOrComposite(id);
    }

    private Condition parseOrComposite(String part) {
        String[] orParts = part.split("\\s+or\\s+");

        if (orParts.length > 1) {
            return Arrays.stream(orParts)
                    .map(String::trim)
                    .map(this::resolveSingleId)
                    .reduce(Condition::or)
                    .orElseThrow();
        }

        return resolveSingleId(part.trim());
    }

    private Condition resolveSingleId(String id) {
        Condition reusable = registry.get(id);
        if (reusable == null) {
            throw new IllegalArgumentException("Unknown reusable condition: " + id);
        }
        return reusable;
    }
}
