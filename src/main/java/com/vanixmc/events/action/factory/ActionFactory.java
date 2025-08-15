package com.vanixmc.events.action.factory;

import com.vanixmc.events.action.clear_variable_action.ClearVariableAction;
import com.vanixmc.events.action.command_action.CommandAction;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.action.domain.ActionType;
import com.vanixmc.events.action.message_action.PlayerMessageAction;
import com.vanixmc.events.action.select_random_action.SelectRandomPlayerAction;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.factory.TriggerFactory;
import lombok.Getter;

import java.util.*;

public class ActionFactory {
    private final Map<ActionType, ConfigBuilder<AbstractAction>> builders;

    @Getter
    private final HashMap<String, Action> registry;

    public ActionFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
        registerAllActionTypes();
    }

    public void registerBuilder(ActionType type, ConfigBuilder<AbstractAction> builder) {
        builders.put(type, builder);
    }

    public void registerAll(Map<String, Map<String, Object>> actions) {
        for (String key : actions.keySet()) {
            Action action = create(key, actions);
            registry.put(key, action);
        }
    }

    public AbstractAction create(String key, Map<String, Map<String, Object>> actions) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, actions);
        ActionType type = ActionType.valueOf(config.getUppercaseString("type"));
        ConfigBuilder<AbstractAction> builder = builders.get(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
        AbstractAction action = builder.build(config);

        List<Object> triggers = config.getObjectList("triggers");

        if (triggers != null) {
            TriggerHolder triggerHolder = TriggerFactory.getInstance()
                    .createTriggerHolder(triggers, action);
            action.getTriggerHolder().populate(triggerHolder);
        }

        return action;
    }

    public void registerAllActionTypes() {
        // Register action builders for all action types
        registerBuilder(ActionType.CLEAR_VARIABLE_ACTION, ClearVariableAction.builder());
        registerBuilder(ActionType.SELECT_RANDOM_PLAYER, SelectRandomPlayerAction.builder());
        registerBuilder(ActionType.PLAYER_MESSAGE, PlayerMessageAction.builder());
        registerBuilder(ActionType.COMMAND, CommandAction.builder());
    }

    public ActionHolder createActionHolder(List<Object> actions, Event event) {
        List<Action> resolvedActions = new ArrayList<>();

        for (Object item : actions) {
            if (item instanceof Map<?, ?> map) {
                System.out.println(map);
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
                    AbstractAction inlineAction = create(tempKey, wrapper);
                    resolvedActions.add(inlineAction);
                    inlineAction.setEvent(event);
                }
            } else {
                throw new IllegalArgumentException("Action entry must be a map: " + item);
            }
        }
        return new ActionHolder(resolvedActions);
    }

}