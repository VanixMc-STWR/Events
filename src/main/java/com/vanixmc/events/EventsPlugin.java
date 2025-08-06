package com.vanixmc.events;

import co.aikar.commands.BukkitCommandManager;
import com.vanixmc.events.action.factory.ActionFactory;
import com.vanixmc.events.commands.EventsCommand;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.event.registry.EventRegistry;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EventsPlugin extends JavaPlugin {

    @Getter
    public static EventsPlugin instance;
    private EventRegistry eventRegistry;
    private ConditionFactory conditionFactory;
    private ActionFactory actionFactory;

    @Override
    public void onEnable() {
        instance = this;

        this.conditionFactory = new ConditionFactory();
        this.actionFactory = new ActionFactory();
        this.eventRegistry = new EventRegistry();

        // Plugin startup logic
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new EventsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
