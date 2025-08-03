package com.vanixmc.events.action;

import com.vanixmc.events.action.command_action.CommandAction;
import com.vanixmc.events.action.message_action.PlayerMessageAction;
import com.vanixmc.events.shared.DomainConfig;
import lombok.Getter;

import java.util.*;

public class ActionFactory {
    private final Map<ActionType, ActionBuilder> registry;

    @Getter
    private final LinkedHashMap<String, Action> reusableActions;

    public ActionFactory(Map<String, Action> reusableActions) {
        this.registry = new HashMap<>();
        this.reusableActions = new LinkedHashMap<>(reusableActions);
    }

    public void register(ActionType type, ActionBuilder builder) {
        registry.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> actions) {
        for (String key : actions.keySet()) {
            Action action = create(key, actions);
            reusableActions.put(key, action);
        }
        System.out.println(reusableActions);
    }

    public Action create(String key, Map<String, Map<String, Object>> actions) {
        DomainConfig config = resolveConfig(key, actions);
        ActionType type = ActionType.valueOf(config.getUppercaseString("type"));
        ActionBuilder builder = registry.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
        return builder.build(config);
    }

    public void registerAllActionTypes() {
        // Register action builders for all action types
        register(ActionType.PLAYER_MESSAGE, PlayerMessageAction.builder());
        register(ActionType.COMMAND, CommandAction.builder());
    }

    /**
     * Recursively resolves inheritance for actions by merging parent and child configurations.
     * If the action has an "id" key, it inherits from the parent action with that id.
     * Child properties override parent properties.
     *
     * @param key     the action key to resolve
     * @param actions the map of all actions
     * @return the merged ActionConfig
     */
    private DomainConfig resolveConfig(String key, Map<String, Map<String, Object>> actions) {
        Map<String, Object> raw = new HashMap<>(actions.get(key));
        if (raw.containsKey("id")) {
            // Inherit from parent action
            String parentId = (String) raw.get("id");
            DomainConfig parent = resolveConfig(parentId, actions);
            Map<String, Object> parentMap = new HashMap<>(parent.getConfig());
            parentMap.putAll(raw); // Child overrides parent
            DomainConfig merged = new DomainConfig();
            merged.getConfig().putAll(parentMap);
            return merged;
        } else {
            // No inheritance, just use raw config
            DomainConfig config = new DomainConfig();
            config.getConfig().putAll(raw);
            return config;
        }
    }

    public ActionHolder createActionHolder(List<Object> actions) {
        List<Object> resolvedActions = new ArrayList<>();

        for (Object item : actions) {
            if (item instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> actionData = (Map<String, Object>) map;

                // Reference by id
                if (actionData.containsKey("id") && actionData.size() == 1) {
                    String id = (String) actionData.get("id");
                    Action reusable = reusableActions.get(id);
                    if (reusable == null) {
                        throw new IllegalArgumentException("Unknown reusable action: " + id);
                    }
                    resolvedActions.add(reusable);
                } else {
                    // Inline action
                    String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
                    Map<String, Map<String, Object>> wrapper = Map.of(tempKey, actionData);
                    Action inlineAction = create(tempKey, wrapper);
                    resolvedActions.add(inlineAction);
                }
            } else {
                throw new IllegalArgumentException("Action entry must be a map: " + item);
            }
        }
        return new ActionHolder(resolvedActions);
    }

}