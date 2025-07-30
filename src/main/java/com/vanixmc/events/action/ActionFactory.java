package com.vanixmc.events.action;

import com.vanixmc.events.action.command_action.CommandAction;
import com.vanixmc.events.action.command_action.CommandSender;
import com.vanixmc.events.action.message_action.MessageFormat;
import com.vanixmc.events.action.message_action.PlayerMessageAction;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ActionFactory {
    private final Map<ActionType, ActionMeta> registry;
    @Getter
    private final LinkedHashMap<String, Action> reusableActions;

    public ActionFactory(Map<String, Action> reusableActions) {
        this.registry = new HashMap<>();
        this.reusableActions = new LinkedHashMap<>(reusableActions);
    }

    public void register(ActionType type, ActionBuilder builder) {
        registry.put(type, new ActionMeta(type, builder));
    }

    public void registerAll(Map<String, Map<String, Object>> actions) {
        for (String key : actions.keySet()) {
            Action action = create(key, actions);
            reusableActions.put(key, action);
        }
        System.out.println(reusableActions);
    }

    public Action create(String key, Map<String, Map<String, Object>> actions) {
        ActionConfig config = resolveConfig(key, actions);
        ActionType type = ActionType.valueOf(config.getUppercaseString("type"));
        ActionMeta meta = registry.get(type);

        if (meta == null) {
            throw new IllegalArgumentException("Unknown action type: " + type);
        }
        return meta.create(config);
    }

    public void registerAllActionTypes() {
        // Register player message loading
        register(ActionType.PLAYER_MESSAGE, config -> {
            MessageFormat format = MessageFormat.valueOf(config.getUppercaseString("format"));
            String message = config.getString("message");

            if (format == MessageFormat.TITLE) {
                String subtitle = config.getString("subtitle");
                Integer fadeInTicks = config.getInt("fadeInTicks");
                Integer fadeOutTicks = config.getInt("fadeOutTicks");
                Integer stayTicks = config.getInt("stayTicks");

                if ((fadeInTicks != null || fadeOutTicks != null || stayTicks != null) &&
                        (fadeInTicks == null || fadeOutTicks == null || stayTicks == null)) {
                    System.out.println("Warning: If one of fadeInTicks, fadeOutTicks, or stayTicks is set, all must be set.");
                    return new PlayerMessageAction(format, message, subtitle);
                } else {
                    return new PlayerMessageAction(format, message, subtitle, fadeInTicks, fadeOutTicks, stayTicks);
                }
            }
            return new PlayerMessageAction(format, message);
        });

        // Register command action
        register(ActionType.COMMAND, config -> {
            CommandSender sender = CommandSender.valueOf(config.getUppercaseString("sender"));
            String command = config.getString("command");
            if (command.isEmpty()) {
                throw new IllegalArgumentException("Command cannot be null or empty.");
            }
            return new CommandAction(sender, command);
        });
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
    private ActionConfig resolveConfig(String key, Map<String, Map<String, Object>> actions) {
        Map<String, Object> raw = new HashMap<>(actions.get(key));
        if (raw.containsKey("id")) {
            // Inherit from parent action
            String parentId = (String) raw.get("id");
            ActionConfig parent = resolveConfig(parentId, actions);
            Map<String, Object> parentMap = new HashMap<>(parent.getConfig());
            parentMap.putAll(raw); // Child overrides parent
            ActionConfig merged = new ActionConfig();
            merged.getConfig().putAll(parentMap);
            return merged;
        } else {
            // No inheritance, just use raw config
            ActionConfig config = new ActionConfig();
            config.getConfig().putAll(raw);
            return config;
        }
    }
}