package com.vanixmc.events;

import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.action.domain.ActionHolder;
import com.vanixmc.events.action.factory.ActionFactory;
import com.vanixmc.events.action.message_action.MessageFormat;
import com.vanixmc.events.action.message_action.PlayerMessageAction;
import com.vanixmc.events.condition.domain.Condition;
import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.condition.factory.ConditionFactory;
import com.vanixmc.events.condition.permission_condition.PermissionCondition;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.event.domain.EventType;
import com.vanixmc.events.event.zone_event.ZoneEvent;
import com.vanixmc.events.shared.DomainConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class EventLoadTest {
    private ActionFactory actionFactory;
    private ConditionFactory conditionFactory;

    @BeforeEach
    public void setup() {
        // Initialize ActionFactory
        this.actionFactory = new ActionFactory();
        this.actionFactory.registerAllActionTypes();

        // Load and register reusable actions
        Yaml yaml = new Yaml();
        InputStream actionsStream = getClass().getClassLoader().getResourceAsStream("actions.yml");
        Map<String, Object> actionData = yaml.load(actionsStream);
        assertNotNull(actionData);

        Map<String, Map<String, Object>> actions = (Map<String, Map<String, Object>>) actionData.get("actions");
        actionFactory.registerAll(actions);

        // Initialize ConditionFactory
        this.conditionFactory = new ConditionFactory();
        this.conditionFactory.registerAllConditionTypes();

        // Load and register reusable conditions
        InputStream conditionsStream = getClass().getClassLoader().getResourceAsStream("conditions.yml");
        Map<String, Object> conditionData = yaml.load(conditionsStream);
        assertNotNull(conditionData);

        Map<String, Map<String, Object>> conditions = (Map<String, Map<String, Object>>) conditionData.get("conditions");
        conditionFactory.registerAll(conditions);
    }

    @Test
    public void testEventLoadsWithActions() {
        // Load the test event from YAML
        Yaml yaml = new Yaml();
        InputStream eventStream = getClass().getClassLoader().getResourceAsStream("test-event.yml");
        Map<String, Object> eventData = yaml.load(eventStream);
        assertNotNull(eventData);

        // Create a DomainConfig from the event data
        DomainConfig config = new DomainConfig();
        config.getConfig().putAll(eventData);

        // Verify event type
        assertEquals(EventType.ZONE, EventType.valueOf(config.getUppercaseString("type")));
        assertEquals("on_enter", config.getString("trigger"));

        // Extract actions from config
        List<Object> actionsList = config.getObjectList("actions");
        assertNotNull(actionsList);
        assertEquals(2, actionsList.size());

        // Create ActionHolder with the actions
        ActionHolder actionHolder = actionFactory.createActionHolder(actionsList);
        assertNotNull(actionHolder);

        // Build the event (normally this would use EventRegistry and builder pattern)
        Event event = ZoneEvent.build("test-event", actionFactory, conditionFactory).build(config);
        assertNotNull(event);
        assertInstanceOf(ZoneEvent.class, event);

        // Verify actions in the event's ActionHolder
        ActionHolder eventActionHolder = event.getActionHolder();
        assertNotNull(eventActionHolder);

        List<Action> actions = eventActionHolder.getActions();
        System.out.println(actions);
        assertEquals(2, actions.size());

        // Verify the first action (from reusable epic-reward)
        Action firstAction = actions.getFirst();
        assertInstanceOf(PlayerMessageAction.class, firstAction);
        PlayerMessageAction messageAction1 = (PlayerMessageAction) firstAction;
        assertEquals(MessageFormat.CHAT, messageAction1.getFormat());
        assertEquals("Hello, Epic World!!!", messageAction1.getMessage());

        // Verify the second action (inline PlayerMessage)
        Action secondAction = actions.get(1);
        assertInstanceOf(PlayerMessageAction.class, secondAction);
        PlayerMessageAction messageAction2 = (PlayerMessageAction) secondAction;
        assertEquals(MessageFormat.TITLE, messageAction2.getFormat());
        assertEquals("You've entered the region!", messageAction2.getMessage());

        System.out.println(event);
    }

    @Test
    public void testEventLoadsWithConditions() {
        // Load the test event from YAML
        Yaml yaml = new Yaml();
        InputStream eventStream = getClass().getClassLoader().getResourceAsStream("test-event.yml");
        Map<String, Object> eventData = yaml.load(eventStream);
        assertNotNull(eventData);

        // Create a DomainConfig from the event data
        DomainConfig config = new DomainConfig();
        config.getConfig().putAll(eventData);

        // Verify event type and basic properties
        assertEquals(EventType.ZONE, EventType.valueOf(config.getUppercaseString("type")));
        assertEquals("on_enter", config.getString("trigger"));

        // Extract conditions from config
        List<Object> conditionsList = config.getObjectList("conditions");
        assertNotNull(conditionsList);
        assertEquals(2, conditionsList.size());

        // Create ConditionHolder with the conditions
        ConditionHolder conditionHolder = conditionFactory.createConditionHolder(conditionsList);
        assertNotNull(conditionHolder);

        // Verify conditions loaded correctly
        List<Condition> conditions = conditionHolder.getConditions();
        assertEquals(2, conditions.size());

        // First condition should be a composite AND condition
        Condition firstCondition = conditions.getFirst();
        // This should be a composite "AND" condition (player-has-perms and test2)
        // We can't directly assert the type since it's likely an anonymous class from the and() method
        // But we can check if it's not a PermissionCondition
        assertFalse(firstCondition instanceof PermissionCondition);

        // Second condition should be a simple permission condition
        Condition secondCondition = conditions.get(1);
        assertInstanceOf(PermissionCondition.class, secondCondition);
        PermissionCondition permCondition = (PermissionCondition) secondCondition;
        assertEquals("test.perm", permCondition.getPermission());

        // Build the event with the action and condition factories
        Event event = ZoneEvent.build("test-event", actionFactory, conditionFactory)
                .build(config);

        assertNotNull(event);
        assertInstanceOf(ZoneEvent.class, event);

        // Verify the event has the correct conditions
        ConditionHolder eventConditionHolder = event.getConditionHolder();
        assertNotNull(eventConditionHolder);

        List<Condition> eventConditions = eventConditionHolder.getConditions();
        assertEquals(2, eventConditions.size());

        System.out.println(conditionHolder);
    }
}
