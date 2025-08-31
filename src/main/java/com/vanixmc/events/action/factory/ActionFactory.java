package com.vanixmc.events.action.factory;

import com.vanixmc.events.action.core_actions.*;
import com.vanixmc.events.action.core_actions.command_action.CommandAction;
import com.vanixmc.events.action.core_actions.message_action.MessageAction;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.AbstractFactory;
import com.vanixmc.events.shared.BuilderKey;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import com.vanixmc.events.trigger.factory.TriggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActionFactory extends AbstractFactory<AbstractAction, Action> {

    public void registerAll(Map<String, Map<String, Object>> actions, Event event) {
        for (String key : actions.keySet()) {
            Action action = create(key, actions, event);
            registry.put(key, action);
        }
    }

    public ActionHolder createActionHolder(List<Object> actions, Event event) {
        List<Action> resolvedActions = new ArrayList<>();

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
                    AbstractAction inlineAction = create(tempKey, wrapper, event);
                    resolvedActions.add(inlineAction);
                    inlineAction.setEvent(event);
                }
            } else {
                throw new IllegalArgumentException("Action entry must be a map: " + item);
            }
        }
        return new ActionHolder(resolvedActions);
    }

    @Override
    public void registerAllBuilders() {
        registerBuilder(BuilderKey.of("region_highlight", "rg_highlight", "rg_border"), RegionHighlightAction.builder());
        registerBuilder(BuilderKey.of("clear_variable", "clear_v", "cv"), ClearVariableAction.builder());
        registerBuilder(BuilderKey.of("select_random_player", "sel_rand_pl"), SelectRandomPlayerAction.builder());
        registerBuilder(BuilderKey.of("message", "msg"), MessageAction.builder());
        registerBuilder(BuilderKey.of("command", "cmd"), CommandAction.builder());
        registerBuilder(BuilderKey.of("give_item", "item_give", "give_i"), GiveItemAction.builder());
        registerBuilder(BuilderKey.of("play_sound", "play_s", "ps"), PlaySoundAction.builder());
    }

    @Override
    public AbstractAction create(String key, Map<String, Map<String, Object>> actions, Event event) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, actions);
        String type = config.getString("type");
        ConfigBuilder<AbstractAction> builder = this.getBuilder(type);

        AbstractAction action = builder.build(config);

        List<Object> triggers = config.getObjectList("triggers");

        if (triggers != null) {
            TriggerHolder triggerHolder = TriggerFactory.getInstance()
                    .createTriggerHolder(triggers, action);
            action.getTriggerHolder().populate(triggerHolder);
        }

        List<Object> conditions = config.getObjectList("conditions");
        ConditionHolder conditionHolder = ConditionFactory.getInstance()
                .createConditionHolder(conditions, event);
        action.getConditionHolder().populate(conditionHolder);

        return action;
    }

    //#region Lazy Initialization
    public static ActionFactory getInstance() {
        return ActionFactory.InstanceHolder.instance;
    }

    private static final class InstanceHolder {
        private static final ActionFactory instance = new ActionFactory();
    }
    //#endregion

}