package com.vanixmc.events.util;

import org.bukkit.entity.EntityType;

public class EntityUtils {
    public static boolean isValidEntityType(String input) {
        try {
            EntityType type = EntityType.valueOf(input.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
