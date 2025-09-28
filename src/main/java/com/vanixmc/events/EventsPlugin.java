package com.vanixmc.events;

import co.aikar.commands.BukkitCommandManager;
import com.vanixmc.events.commands.EventsCommand;
import com.vanixmc.events.event.factory.EventFactory;
import com.vanixmc.events.listeners.PlayerMoveListener;
import com.vanixmc.events.listeners.RegionInteractListener;
import hm.zelha.particlesfx.util.ParticleSFX;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class EventsPlugin extends JavaPlugin {

    @Getter
    private static EventsPlugin instance;

    private static Economy economy = null;

    @Override
    public void onEnable() {
        instance = this;
        initFactories();
        registerCommands();
        registerListeners();
        setupEconomy();
        ParticleSFX.setPlugin(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Economy getEconomy() {
        if (economy != null) {
            return economy;
        }
        throw new RuntimeException("You need the Vault plugin for economy support!");
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

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = getServer()
                .getServicesManager()
                .getRegistration(Economy.class);
        if (rsp == null) return;
        economy = rsp.getProvider();
    }
}
