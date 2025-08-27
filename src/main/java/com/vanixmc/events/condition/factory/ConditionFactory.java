package com.vanixmc.events.condition.factory;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.permission_condition.PermissionCondition;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.AbstractFactory;
import com.vanixmc.events.shared.BuilderKey;
import lombok.Getter;

import java.util.*;

@Getter
public class ConditionFactory extends AbstractFactory<Condition, Condition> {

    @Override
    public void registerAllBuilders() {
        registerBuilder(BuilderKey.of("permission", "perm"), PermissionCondition.builder());
    }

    public ConditionHolder createConditionHolder(List<Object> conditions, Event event) {
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
                    Condition inlineCondition = create(tempKey, wrapper, event);
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
        Condition reusable = super.registry.get(id);
        if (reusable == null) {
            throw new IllegalArgumentException("Unknown reusable condition: " + id);
        }
        return reusable;
    }

    //#region Lazy Initialization
    public static ConditionFactory getInstance() {
        return InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        private static final ConditionFactory instance = new ConditionFactory();
    }
    //#endregion
}
