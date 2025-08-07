package com.vanixmc.events.action.command_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@ToString
public class CommandAction extends AbstractAction {
    private final CommandSender commandSender;
    private final String command;

    @Override
    public boolean execute(EventContext context) {
        return switch (commandSender) {
            case PLAYER -> {
                Player player = context.player();
                if (player == null)
                    throw new RuntimeException("Player is null in context. (ActionCommand with " + commandSender + " command sender)");

                Bukkit.getServer().dispatchCommand(player, command);
                yield true;
            }
            case CONSOLE -> {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                yield true;
            }
        };
    }

    public static ConfigBuilder<Action> builder() {
        return config -> {
            CommandSender sender = CommandSender.valueOf(config.getUppercaseString("sender"));
            String command = config.getString("command");
            if (command.isEmpty()) {
                throw new IllegalArgumentException("Command cannot be null or empty.");
            }
            return new CommandAction(sender, command);
        };
    }
}
