package com.vanixmc.events.condition.conditions;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@Getter
@ToString
@AllArgsConstructor
public class PermissionCondition implements Condition {
    private final String permission;

    @Override
    public boolean test(Context context) {
        Player player = context.getPlayer();
        if (player == null) return false;

        return player.hasPermission(permission);
    }

    public static ConfigBuilder<Condition> builder() {
        return config -> {
            String permission = config.getString("permission");
            if (permission.isEmpty()) {
                throw new IllegalArgumentException("Permission cannot be null or empty.");
            }
            return new PermissionCondition(permission);
        };
    }
}
