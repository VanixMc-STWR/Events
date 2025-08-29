package com.vanixmc.events.action.core_actions.message_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.Chat;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Getter
@ToString
public class MessageAction extends AbstractAction {
    private final MessageFormat format;
    private final String message;
    private final MessageAudience audience;
    private String subtitle = "";
    private int fadeInTicks = 20;
    private int fadeOutTicks = 20;
    private int stayTicks = 20;

    public MessageAction(MessageFormat format, String message, MessageAudience audience) {
        this.format = format;
        this.message = message;
        this.audience = audience;
    }

    public MessageAction(MessageFormat format, String message, MessageAudience audience, String subtitle) {
        this.format = format;
        this.message = message;
        this.audience = audience;
        this.subtitle = subtitle;
    }

    public MessageAction(MessageFormat format, String message, MessageAudience audience,
                         String subtitle, int fadeInTicks, int fadeOutTicks, int stayTicks) {
        this.format = format;
        this.message = message;
        this.audience = audience;
        this.subtitle = subtitle;
        this.fadeInTicks = fadeInTicks;
        this.fadeOutTicks = fadeOutTicks;
        this.stayTicks = stayTicks;
    }

    @Override
    public boolean execute(Context context) {
        Event event = context.getEvent() != null ? context.getEvent() : getEvent();
        if (event == null) throw new IllegalStateException("You must define action inline to use persistent data");

        String parsedMessage = event.getPersistentData().replaceVariables(this.message);
        String parsedSubtitle = event.getPersistentData().replaceVariables(this.subtitle);

        Collection<Player> recipients = determineRecipients(context);
        if (recipients.isEmpty()) {
            return false;
        }

        for (Player recipient : recipients) {
            if (format == MessageFormat.TITLE) {
                recipient.sendTitle(Chat.colorize(parsedMessage), Chat.colorize(parsedSubtitle),
                        fadeInTicks, stayTicks, fadeOutTicks);
            } else {
                Chat.tell(recipient, format, parsedMessage);
            }
        }
        return true;
    }

    private Collection<Player> determineRecipients(Context context) {
        switch (audience) {
            case PLAYER:
                Player player = context.getPlayer();
                if (player == null) {
                    throw new RuntimeException("Player null in event context for PLAYER audience.");
                }
                return Collections.singletonList(player);
            case ALL:
                return new HashSet<>(Bukkit.getOnlinePlayers());
            case VARIABLE:
                String playerName = context.getEvent().getPersistentData().replaceVariables(message);
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    return Collections.singletonList(targetPlayer);
                }
                return Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            MessageFormat format = MessageFormat.valueOf(config.getUppercaseString("format"));
            String message = config.getString("message");

            // Default to PLAYER if not specified
            MessageAudience audience = MessageAudience.PLAYER;

            try {
                audience = MessageAudience.valueOf(config.getUppercaseString("audience"));
            } catch (IllegalArgumentException e) {
                // Default to PLAYER if invalid audience
            }

            if (format == MessageFormat.TITLE) {
                String subtitle = config.getString("subtitle");
                Integer fadeInTicks = config.getInt("fadeInTicks");
                Integer fadeOutTicks = config.getInt("fadeOutTicks");
                Integer stayTicks = config.getInt("stayTicks");

                if (fadeInTicks == null || fadeOutTicks == null || stayTicks == null) {
                    return new MessageAction(format, message, audience, subtitle);
                } else {
                    return new MessageAction(format, message, audience, subtitle,
                            fadeInTicks, fadeOutTicks, stayTicks);
                }
            }
            return new MessageAction(format, message, audience);
        };
    }
}