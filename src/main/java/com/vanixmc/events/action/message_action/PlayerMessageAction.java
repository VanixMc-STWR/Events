package com.vanixmc.events.action.message_action;

import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.Chat;
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
        Player player = context.player();
        if (player == null) throw new RuntimeException("Player null in event context.");

        if (format == MessageFormat.TITLE) {
            player.sendTitle(message, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
            return;
        }
        Chat.tell(player, format, message);
    }

    public static ConfigBuilder<Action> builder() {
        return config -> {
            MessageFormat format = MessageFormat.valueOf(config.getUppercaseString("format"));
            String message = config.getString("message");

            if (format == MessageFormat.TITLE) {
                String subtitle = config.getString("subtitle");
                Integer fadeInTicks = config.getInt("fadeInTicks");
                Integer fadeOutTicks = config.getInt("fadeOutTicks");
                Integer stayTicks = config.getInt("stayTicks");

                if (fadeInTicks == null || fadeOutTicks == null || stayTicks == null) {
                    return new PlayerMessageAction(format, message, subtitle);
                } else {
                    return new PlayerMessageAction(format, message, subtitle, fadeInTicks, fadeOutTicks, stayTicks);
                }
            }
            return new PlayerMessageAction(format, message);
        };
    }
}
