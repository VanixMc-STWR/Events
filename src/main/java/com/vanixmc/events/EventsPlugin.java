package com.vanixmc.events;

import co.aikar.commands.BukkitCommandManager;
import com.vanixmc.events.commands.EventsCommand;
import com.vanixmc.events.event.factory.EventFactory;
import com.vanixmc.events.listeners.PlayerMoveListener;
import com.vanixmc.events.listeners.RegionInteractListener;
import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EventsPlugin extends JavaPlugin {

    @Getter
    private static EventsPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        initFactories();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initFactories() {
        EventFactory.getInstance().loadAllEvents();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerMoveListener(), this);
        pluginManager.registerEvents(new RegionInteractListener(EventFactory.getInstance()), this);
    }

    private void registerCommands() {
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new EventsCommand());
    }
}
