package com.vanixmc.events.condition.domain;

import com.vanixmc.events.condition.evaluator.Evaluator;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import org.bukkit.Bukkit;

/**
 * Base class for conditions that use an Evaluator.
 */
public abstract class EvaluatingCondition implements Condition {
    protected final Evaluator evaluator;

    protected EvaluatingCondition(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * Implement this to provide the "actual value" from the context
     * (e.g., balance, level, health).
     */
    protected Object getActual(Context context) {
        return null;
    }

    /**
     * Implement this to provide the "expected value"
     * (e.g., required amount).
     */
    protected Object getExpected() {
        return null;
    }

    /**
     * Builder helper that injects the evaluator automatically.
     */
    protected static ConfigBuilder<Condition> buildWithEvaluator(EvalConditionBuilder factory) {
        return config -> {
            String evaluatorString = config.getUppercaseString("evaluator");
            Evaluator evaluator;
            try {
                evaluator = Evaluator.valueOf(evaluatorString);
            } catch (IllegalArgumentException e) {
                Bukkit.getServer()
                        .getLogger()
                        .info("[StarEvents] no 'evaluator' was provided for condition" + EvaluatingCondition.class.getCanonicalName() +
                                " so 'EQUAL | TRUE' was assumed.");
                evaluator = Evaluator.EQUAL;
            }
            return factory.create(config, evaluator);
        };
    }

    @FunctionalInterface
    public interface EvalConditionBuilder {
        Condition create(DomainConfig config, Evaluator evaluator);
    }

    @Override
    public boolean test(Context context) {
        return evaluator.evaluate(getActual(context), getExpected());
    }
}
