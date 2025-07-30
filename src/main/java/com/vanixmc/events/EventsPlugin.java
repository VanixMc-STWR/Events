package com.vanixmc.events;

import com.vanixmc.events.commands.eventscommand;
import co.aikar.commands.BukkitCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        BukkitCommandManager commandManager = new BukkitCommandManager(this);
        commandManager.registerCommand(new eventscommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
