package com.vanixmc.events.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@Builder
@AllArgsConstructor
public class EventContext {
    private final Player player;
}
