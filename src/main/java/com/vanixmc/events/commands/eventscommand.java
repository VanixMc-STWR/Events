package com.vanixmc.events.commands;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import org.bukkit.entity.Player;

@CommandAlias("starevents|sevents|ste")
public class eventscommand extends BaseCommand {

    @Default
    public void onRunCommand(Player player) {
        player.sendMessage("This command and it's aliases are working!"); // Test message
    }
}
