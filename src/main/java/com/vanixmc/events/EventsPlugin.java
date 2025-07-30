package com.vanixmc.events;

import com.vanixmc.events.commands.EventsCommand;
import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new EventsCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
