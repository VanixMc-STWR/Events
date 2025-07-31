package com.vanixmc.events.action.command_action;

import com.vanixmc.events.action.Action;
import com.vanixmc.events.event.EventContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@ToString
public class CommandAction implements Action {
    private final CommandSender commandSender;
    private final String command;

    @Override
    public void execute(EventContext context) {
        switch (commandSender) {
            case PLAYER -> {
                Player player = context.getPlayer();
                if (player == null)
                    throw new RuntimeException("Player is null in context. (ActionCommand with " + commandSender + " command sender)");

                Bukkit.getServer().dispatchCommand(player, command);
            }
            case CONSOLE -> Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }
}
