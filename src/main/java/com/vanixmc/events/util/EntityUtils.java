package com.vanixmc.events.util;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.Optional;

public class EntityUtils {
    public static Optional<EntityType> getVanillaEntityType(String entityId) {
        return Optional.of(EntityType.valueOf(entityId.toUpperCase().trim()));
    }

    public static Optional<MythicMob> getMythicMob(String entityId) {
        return MythicBukkit.inst().getMobManager().getMythicMob(entityId);
    }
}
