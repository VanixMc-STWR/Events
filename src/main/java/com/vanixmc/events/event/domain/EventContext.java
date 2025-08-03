package com.vanixmc.events.event.domain;

import lombok.Builder;
import org.bukkit.entity.Player;

@Builder
public record EventContext(Player player, Event event) {}
