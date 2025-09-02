package com.vanixmc.events.action.core_actions;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@ToString
@AllArgsConstructor
public class PlaySoundAction extends AbstractAction {
    private final Sound sound;
    private final boolean global;
    private final float volume;
    private final float pitch;

    @Override
    public boolean execute(Context context) {
        if (global) {
            getGlobalScopePlayers()
                    .forEach(p -> p.playSound(p.getLocation(), sound, volume, pitch));
            return true;
        }
        Player player = context.getPlayer();
        if (player == null) return false;

        player.playSound(player.getLocation(), sound, volume, pitch);
        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String soundName = config.getUppercaseString("sound");
            if (soundName == null) throw new IllegalArgumentException("Sound cannot be null");

            boolean global = false;
            try {
                global = config.getBoolean("global");
            } catch (Exception ignored) {
            }

            float volume = 1.0f;
            String volumeString = config.getString("volume");
            if (volumeString != null) {
                try {
                    volume = Float.parseFloat(volumeString);
                } catch (NumberFormatException ignored) {
                }
            }

            float pitch = 1.0f;
            String pitchString = config.getString("pitch");
            if (pitchString != null) {
                try {
                    pitch = Float.parseFloat(pitchString);
                } catch (NumberFormatException ignored) {
                }
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sound: " + soundName);
            }

            return new PlaySoundAction(sound, global, volume, pitch);
        };
    }
}