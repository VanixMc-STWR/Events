package com.vanixmc.events.action.factory;

import com.vanixmc.events.action.command_action.CommandAction;
import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.action.domain.ActionType;
import com.vanixmc.events.action.message_action.PlayerMessageAction;
import com.vanixmc.events.action.core_actions.PlaySoundAction;
import com.vanixmc.events.action.core_actions.GiveItemAction;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import lombok.Getter;

import java.util.*;

public class ActionFactory {
    private final Map<ActionType, ConfigBuilder<Action>> builders;

    @Getter
    private final HashMap<String, Action> registry;

    public ActionFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
    }

    public void registerBuilder(ActionType type, ConfigBuilder<Action> builder) {
        builders.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> actions) {
        for (String key : actions.keySet()) {
            Action action = create(key, actions);
            registry.put(key, action);
        }
    }

    public Action create(String key, Map<String, Map<String, Object>> actions) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, actions);
        ActionType type = ActionType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<Action> builder = builders.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
        return builder.build(config);
    }

    public void registerAllActionTypes() {
        // Register action builders for all action types
        registerBuilder(ActionType.PLAYER_MESSAGE, PlayerMessageAction.builder());
        registerBuilder(ActionType.COMMAND, CommandAction.builder());
        registerBuilder(ActionType.GIVE_ITEM, GiveItemAction.builder());
        registerBuilder(ActionType.PLAY_SOUND, PlaySoundAction.builder());

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
                    Action reusable = registry.get(id);
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