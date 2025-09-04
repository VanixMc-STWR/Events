package com.vanixmc.events.condition.evaluator;

import java.util.Objects;

/**
 * Enum representing comparison operations for evaluating conditions.
 * Works with {@link Comparable} types (e.g., Number, String, Date).
 */
public enum Evaluator {
    NOT_EQUAL, FALSE,
    EQUAL, TRUE,
    LESS, LESS_OR_EQUAL,
    GREATER, GREATER_OR_EQUAL;

    /**
     * Evaluates the comparison between two objects according to this evaluator.
     * <p>
     * Equality operations:
     * - EQUAL: true if both objects are equal (null-safe).
     * - NOT_EQUAL: true if objects differ (null-safe).
     * <p>
     * Relational operations:
     * - LESS, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL
     * work on {@link Comparable} objects (e.g., Number, String, Date).
     *
     * @param o1 first object
     * @param o2 second object
     * @return result of evaluation
     * @throws IllegalArgumentException if relational operations are used on non-comparable values
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean evaluate(Object o1, Object o2) {
        return switch (this) {
            case EQUAL, TRUE -> Objects.equals(o1, o2);
            case NOT_EQUAL, FALSE -> !Objects.equals(o1, o2);

            case LESS, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL -> {
                if (o1 instanceof Comparable c1 && o2 != null && c1.getClass().isAssignableFrom(o2.getClass())) {
                    int cmp = c1.compareTo(o2);
                    yield switch (this) {
                        case LESS -> cmp < 0;
                        case LESS_OR_EQUAL -> cmp <= 0;
                        case GREATER -> cmp > 0;
                        case GREATER_OR_EQUAL -> cmp >= 0;
                        default -> throw new IllegalStateException("Unexpected case: " + this);
                    };
                }
                throw new IllegalArgumentException("Relational comparison requires comparable values of the same type: "
                        + describe(o1) + " vs " + describe(o2));
            }
        };
    }

    private static String describe(Object obj) {
        return obj == null ? "null" : obj + " (" + obj.getClass().getSimpleName() + ")";
    }
}