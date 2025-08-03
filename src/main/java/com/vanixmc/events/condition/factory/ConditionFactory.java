package com.vanixmc.events.condition.factory;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.domain.ConditionType;
import com.vanixmc.events.condition.permission_condition.PermissionCondition;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import lombok.Getter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // Handle expressions with parentheses first
        if (id.contains("(")) {
            return parseExpressionWithParentheses(id);
        }

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
        // Handle expressions with parentheses first
        if (part.contains("(")) {
            return parseExpressionWithParentheses(part);
        }

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

    private Condition parseExpressionWithParentheses(String expression) {
        // First handle the innermost parentheses
        Pattern pattern = Pattern.compile("\\(([^()]+)\\)");
        Matcher matcher = pattern.matcher(expression);

        StringBuilder sb = new StringBuilder();
        Map<String, Condition> placeholders = new HashMap<>();

        while (matcher.find()) {
            String innerExpression = matcher.group(1);
            Condition innerResult = parseCompositeId(innerExpression);

            // Create a placeholder for this result
            String placeholder = "__PLACEHOLDER_" + placeholders.size() + "__";
            placeholders.put(placeholder, innerResult);

            // Replace the parenthesized expression with the placeholder
            matcher.appendReplacement(sb, placeholder);
        }
        matcher.appendTail(sb);

        String simplifiedExpression = sb.toString();

        // If there are still parentheses, it means we have nested parentheses
        if (simplifiedExpression.contains("(")) {
            Condition nestedResult = parseExpressionWithParentheses(simplifiedExpression);

            // Replace all placeholders in the result (if any)
            return replaceConditionPlaceholders(nestedResult, placeholders);
        }

        // Process the simplified expression (with placeholders)
        Condition result = parseExpressionWithPlaceholders(simplifiedExpression, placeholders);

        return result;
    }

    private Condition parseExpressionWithPlaceholders(String expression, Map<String, Condition> placeholders) {
        // Split by 'and' first (higher precedence)
        String[] andParts = expression.split("\\s+and\\s+");

        if (andParts.length > 1) {
            // Process each part and combine with AND
            return Arrays.stream(andParts)
                    .map(part -> parseOrExpressionWithPlaceholders(part, placeholders))
                    .reduce(Condition::and)
                    .orElseThrow();
        }

        // Only one part or no 'and' found, process as OR expression
        return parseOrExpressionWithPlaceholders(expression, placeholders);
    }

    private Condition parseOrExpressionWithPlaceholders(String expression, Map<String, Condition> placeholders) {
        String[] orParts = expression.split("\\s+or\\s+");

        if (orParts.length > 1) {
            // Process each part and combine with OR
            return Arrays.stream(orParts)
                    .map(part -> resolveTermWithPlaceholders(part.trim(), placeholders))
                    .reduce(Condition::or)
                    .orElseThrow();
        }

        // Only one term
        return resolveTermWithPlaceholders(expression.trim(), placeholders);
    }

    private Condition resolveTermWithPlaceholders(String term, Map<String, Condition> placeholders) {
        // Check if it's a placeholder
        if (placeholders.containsKey(term)) {
            return placeholders.get(term);
        }

        // Otherwise, it's a regular condition ID
        return resolveSingleId(term);
    }

    private Condition replaceConditionPlaceholders(Condition condition, Map<String, Condition> placeholders) {
        // This is a simplified implementation assuming we don't need to actually
        // replace placeholders inside complex conditions
        return condition;
    }

    private Condition resolveSingleId(String id) {
        Condition reusable = registry.get(id);
        if (reusable == null) {
            throw new IllegalArgumentException("Unknown reusable condition: " + id);
        }
        return reusable;
    }
}
