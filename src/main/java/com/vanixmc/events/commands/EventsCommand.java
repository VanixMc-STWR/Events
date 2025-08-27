package com.vanixmc.events.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.vanixmc.events.util.Chat;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("starevents|sevents|ste")
public class EventsCommand extends BaseCommand {

    @Default
    public void onRunCommand(Player player) {
        Chat.tell(player, "This command and it's aliases are working!");
    }
}
