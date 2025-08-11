package com.vanixmc.events.action.core_actions;

import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Getter
@ToString
@AllArgsConstructor
public class PlaySoundAction implements Action {
    private final Sound sound;
    private final boolean global;
    private final float volume;
    private final float pitch;

    @Override
    public void execute(EventContext context) {
        Player player = context.player();
        if (player == null) throw new RuntimeException("The player is null in this context.");

        if (global) {
            Bukkit.getOnlinePlayers().forEach(p ->
                    p.playSound(p.getLocation(), sound, volume, pitch)
            );
        } else {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static ConfigBuilder<Action> builder() {
        return config -> {
            String soundName = config.getUppercaseString("sound");
            if (soundName == null) throw new IllegalArgumentException("Sound cannot be null");

            boolean Global = false;
            try {
                Global = config.getBoolean("global");
            } catch (Exception ignored) {}

            float Volume = 1.0f;
            String volumeString = config.getString("volume");
            if (volumeString != null) {
                try { Volume = Float.parseFloat(volumeString); } catch (NumberFormatException ignored) {}
            }

            float Pitch = 1.0f;
            String pitchString = config.getString("pitch");
            if (pitchString != null) {
                try { Pitch = Float.parseFloat(pitchString); } catch (NumberFormatException ignored) {}
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sound: " + soundName);
            }

            return new PlaySoundAction(sound, Global, Volume, Pitch);
        };
    }
}