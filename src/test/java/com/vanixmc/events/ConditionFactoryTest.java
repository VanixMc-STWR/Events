package com.vanixmc.events;

import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.event.domain.EventContext;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ConditionFactoryTest {

    private ConditionFactory conditionFactory;

    @Mock
    private Player player;

    @Mock
    private EventContext eventContext;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Setup EventContext to return our mocked player
        when(eventContext.player()).thenReturn(player);

        // Initialize the condition factory
        conditionFactory = new ConditionFactory();

        // Register test conditions in the registry
        conditionFactory.getRegistry().put("always-true", new TestCondition(true));
        conditionFactory.getRegistry().put("always-false", new TestCondition(false));
    }

    @Test
    public void testSimpleCondition() {
        // Create a single condition using ID reference
        Map<String, Object> conditionData = Map.of("id", "always-true");

        List<Object> conditionsList = Collections.singletonList(conditionData);
        ConditionHolder holder = conditionFactory.createConditionHolder(conditionsList);

        // Verify
        List<Condition> conditions = holder.getConditions();
        assertEquals(1, conditions.size());
        assertTrue(conditions.getFirst().test(eventContext));
    }

    @Test
    public void testAndCompositeCondition() {
        // Test "true AND false" = false
        Map<String, Object> andCondition = Map.of("id", "always-true and always-false");

        List<Object> conditionsList = Collections.singletonList(andCondition);
        ConditionHolder holder = conditionFactory.createConditionHolder(conditionsList);

        // Verify
        List<Condition> conditions = holder.getConditions();
        assertEquals(1, conditions.size());
        assertFalse(conditions.getFirst().test(eventContext)); // AND with false should be false

        // Test "true AND true" = true
        conditionFactory.getRegistry().put("another-true", new TestCondition(true));
        Map<String, Object> andTrueCondition = Map.of("id", "always-true and another-true");

        List<Object> trueAndList = Collections.singletonList(andTrueCondition);
        ConditionHolder trueAndHolder = conditionFactory.createConditionHolder(trueAndList);

        // Verify
        List<Condition> trueAndConditions = trueAndHolder.getConditions();
        assertEquals(1, trueAndConditions.size());
        assertTrue(trueAndConditions.getFirst().test(eventContext)); // AND with true should be true
    }

    @Test
    public void testOrCompositeCondition() {
        // Test "true OR false" = true
        Map<String, Object> orCondition = Map.of("id", "always-true or always-false");

        List<Object> conditionsList = Collections.singletonList(orCondition);
        ConditionHolder holder = conditionFactory.createConditionHolder(conditionsList);

        // Verify
        List<Condition> conditions = holder.getConditions();
        assertEquals(1, conditions.size());
        assertTrue(conditions.getFirst().test(eventContext)); // OR with true should be true

        // Test "false OR false" = false
        conditionFactory.getRegistry().put("another-false", new TestCondition(false));
        Map<String, Object> orFalseCondition = Map.of("id", "always-false or another-false");

        List<Object> falseOrList = Collections.singletonList(orFalseCondition);
        ConditionHolder falseOrHolder = conditionFactory.createConditionHolder(falseOrList);

        // Verify
        List<Condition> falseOrConditions = falseOrHolder.getConditions();
        assertEquals(1, falseOrConditions.size());
        assertFalse(falseOrConditions.getFirst().test(eventContext)); // OR with false should be false
    }

    @Test
    public void testMultipleAndOr() {
        // Test "true AND false AND true" = false
        Map<String, Object> multipleAndCondition = Map.of("id", "always-true and always-false and always-true");

        List<Object> andConditionsList = Collections.singletonList(multipleAndCondition);
        ConditionHolder andHolder = conditionFactory.createConditionHolder(andConditionsList);

        // Verify
        List<Condition> andConditions = andHolder.getConditions();
        assertEquals(1, andConditions.size());
        assertFalse(andConditions.getFirst().test(eventContext));

        // Test "false OR false OR true" = true
        Map<String, Object> multipleOrCondition = Map.of("id", "always-false or always-false or always-true");

        List<Object> orConditionsList = Collections.singletonList(multipleOrCondition);
        ConditionHolder orHolder = conditionFactory.createConditionHolder(orConditionsList);

        // Verify
        List<Condition> orConditions = orHolder.getConditions();
        assertEquals(1, orConditions.size());
        assertTrue(orConditions.getFirst().test(eventContext));
    }

    /**
     * A simple test condition implementation that returns a predefined result
     * for easy testing of composite conditions
     */
    private record TestCondition(boolean result) implements Condition {
        @Override
        public boolean test(EventContext eventContext) {
            return result;
        }

        @Override
        public String toString() {
            return "TestCondition(" + result + ")";
        }
    }
}
