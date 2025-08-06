package com.vanixmc.events;

import co.aikar.commands.BukkitCommandManager;
import com.vanixmc.events.action.factory.ActionFactory;
import com.vanixmc.events.commands.EventsCommand;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.event.factory.EventFactory;
import com.vanixmc.events.trigger.factory.TriggerFactory;
import com.vanixmc.events.trigger.trigger_modes.factory.TriggerModeFactory;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EventsPlugin extends JavaPlugin {

    @Getter
    private static EventsPlugin instance;
    private EventFactory eventFactory;
    private ConditionFactory conditionFactory;
    private ActionFactory actionFactory;
    private TriggerModeFactory triggerModeFactory;
    private TriggerFactory triggerFactory;

    @Override
    public void onEnable() {
        instance = this;

        this.triggerModeFactory = new TriggerModeFactory();
        this.triggerFactory = new TriggerFactory();
        this.conditionFactory = new ConditionFactory();
        this.actionFactory = new ActionFactory();
        this.eventFactory = new EventFactory();

        // Plugin startup logic
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new EventsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
