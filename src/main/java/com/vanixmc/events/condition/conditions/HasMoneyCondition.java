package com.vanixmc.events.condition.conditions;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.EvaluatingCondition;
import com.vanixmc.events.condition.evaluator.Evaluator;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;

public class HasMoneyCondition extends EvaluatingCondition {
    private final double amount;

    protected HasMoneyCondition(Evaluator evaluator, double amount) {
        super(evaluator);
        this.amount = amount;
    }

    @Override
    protected Object getActual(Context context) {
        return EventsPlugin.getEconomy().getBalance(context.getPlayer());
    }

    @Override
    protected Object getExpected() {
        return amount;
    }

    public static ConfigBuilder<Condition> builder() {
        return buildWithEvaluator((config, evaluator) -> {
            Double amount = config.getDouble("amount");
            if (amount == null) {
                throw new IllegalArgumentException("'amount' cannot be null or empty.");
            }
            return new HasMoneyCondition(evaluator, amount);
        });
    }
}
