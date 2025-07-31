package com.vanixmc.events.action.message_action;

import com.vanixmc.events.action.Action;
import com.vanixmc.events.event.EventContext;
import com.vanixmc.events.utils.Chat;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@Getter
@ToString
public class PlayerMessageAction implements Action {
    private final MessageFormat format;
    private final String message;
    private String subtitle = "";
    private int fadeInTicks = 20;
    private int fadeOutTicks = 20;
    private int stayTicks = 20;

    public PlayerMessageAction(MessageFormat format, String message) {
        this.format = format;
        this.message = message;
    }

    public PlayerMessageAction(MessageFormat format, String message, String subtitle) {
        this.format = format;
        this.message = message;
        this.subtitle = subtitle;
    }

    public PlayerMessageAction(MessageFormat format, String message, String subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks) {
        this.format = format;
        this.message = message;
        this.subtitle = subtitle;
        this.fadeInTicks = fadeInTicks;
        this.fadeOutTicks = fadeOutTicks;
        this.stayTicks = stayTicks;
    }

    @Override
    public void execute(EventContext context) {
        Player player = context.getPlayer();
        if (player == null) throw new RuntimeException("Player null in event context.");

        if (format == MessageFormat.TITLE) {
            player.sendTitle(message, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
            return;
        }
        Chat.tell(player, format, message);
    }
}
