package com.vanixmc.events.condition.conditions;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.EvaluatingCondition;
import com.vanixmc.events.condition.evaluator.Evaluator;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.entity.Player;

@Getter
@ToString
public class PermissionCondition extends EvaluatingCondition {
    private final String permission;

    public PermissionCondition(Evaluator evaluator, String permission) {
        super(evaluator);
        this.permission = permission;
    }

    @Override
    protected Object getExpected() {
        return true;
    }

    @Override
    protected Object getActual(Context context) {
        Player player = context.getPlayer();
        if (player == null) return null;

        return player.hasPermission(permission);
    }

    public static ConfigBuilder<Condition> builder() {
        return buildWithEvaluator((config, evaluator) -> {
            String permission = config.getString("permission");
            if (permission.isEmpty()) {
                throw new IllegalArgumentException("Permission cannot be null or empty.");
            }
            return new PermissionCondition(evaluator, permission);
        });
    }
}
