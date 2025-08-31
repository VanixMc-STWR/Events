//package com.vanixmc.events;
//
//import com.vanixmc.events.action.domain.ActionHolder;
//import com.vanixmc.events.action.factory.ActionFactory;
//import com.vanixmc.events.condition.domain.ConditionHolder;
//import com.vanixmc.events.condition.factory.ConditionFactory;
//import com.vanixmc.events.event.domain.Event;
//import com.vanixmc.events.event.domain.EventType;
//import com.vanixmc.events.shared.ConfigBuilder;
//import com.vanixmc.events.shared.DomainConfig;
//import com.vanixmc.events.trigger.domain.AbstractTrigger;
//import com.vanixmc.events.trigger.domain.Trigger;
//import com.vanixmc.events.trigger.domain.TriggerHolder;
//import com.vanixmc.events.trigger.factory.TriggerFactory;
//import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
//import com.vanixmc.events.trigger.trigger_modes.factory.TriggerModeFactory;
//import org.bukkit.Bukkit;
//import org.bukkit.Server;
//import org.bukkit.UnsafeValues;
//import org.bukkit.World;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.mockito.Mockito.when;
//
//public class EventLoadTest {
//    private ActionFactory actionFactory;
//    private ConditionFactory conditionFactory;
//
//    @Mock
//    EventsPlugin eventsPlugin;
//
//    @Mock
//    Server server;
//
//    @Mock
//    World world;
//
//    @Mock
//    UnsafeValues unsafeValues;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testEventLoadsWithLocation() {
//        try (MockedStatic<Bukkit> bukkit = Mockito.mockStatic(Bukkit.class)) {
//            bukkit.when(Bukkit::getServer).thenReturn(server);
//            when(Bukkit.getWorld("world")).thenReturn(world);
//            System.out.println(loadEvent("test-event.yml"));
//        }
//    }
//
//    private Event loadEvent(String fileName) {
//        Map<String, Object> data = load(fileName);
//        DomainConfig config = new DomainConfig(data);
//
//        // Put the id in so it's in the built event
//        config.getConfig().put("id", fileName);
//
//        EventType type = EventType.valueOf(config.getUppercaseString("type"));
//        ConfigBuilder<Event> builder = type.getBuilder();
//
//        if (builder == null) {
//            throw new IllegalArgumentException("Unknown action type: " + type);
//        }
//
//        Event event = builder.build(config);
//        List<Object> triggers = config.getObjectList("triggers");
//        List<Object> conditions = config.getObjectList("conditions");
//        List<Object> actions = config.getObjectList("actions");
//
//        ConditionHolder conditionHolder = ConditionFactory.getInstance().createConditionHolder(conditions, event);
//        ActionHolder actionHolder = ActionFactory.getInstance().createActionHolder(actions, event);
//
//        TriggerHolder triggerHolder = createTriggerHolder(triggers);
//        event.getConditionHolder().populate(conditionHolder);
//        event.getActionHolder().populate(actionHolder);
//        event.getTriggerHolder().populate(triggerHolder);
//
//        return event;
//    }
//
//    private Map<String, Object> load(String name) {
//        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(name)) {
//            Yaml yml = new Yaml();
//            return yml.load(inputStream);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private TriggerHolder createTriggerHolder(List<Object> triggers) {
//        List<Trigger> resolvedTriggers = new ArrayList<>();
//
//        for (Object item : triggers) {
//            if (item instanceof Map<?, ?> map) {
//                @SuppressWarnings("unchecked")
//                Map<String, Object> triggerData = (Map<String, Object>) map;
//
//                // Inline trigger
//                String tempKey = UUID.randomUUID().toString(); // Temp key for resolving
//                Map<String, Map<String, Object>> wrapper = Map.of(tempKey, triggerData);
//                Trigger inlineTrigger = create(tempKey, wrapper);
//                resolvedTriggers.add(inlineTrigger);
//            } else {
//                throw new IllegalArgumentException("Trigger entry must be a map: " + item);
//            }
//        }
//        return new TriggerHolder(resolvedTriggers);
//    }
//
//    private AbstractTrigger create(String key, Map<String, Map<String, Object>> fileData) {
//        DomainConfig config = ConfigBuilder.resolveConfig(key, fileData);
//        String type = config.getString("type");
//        ConfigBuilder<AbstractTrigger> builder = TriggerFactory.getInstance().getBuilder(type);
//
//        if (builder == null) {
//            throw new IllegalArgumentException("Unknown condition type: " + type);
//        }
//
//        Object triggerModeFromConfig = config.getObject("mode");
//        TriggerMode triggerMode;
//
//        if (triggerModeFromConfig == null) {
//            ConfigBuilder<TriggerMode> triggerModeBuilder = TriggerModeFactory.getInstance()
//                    .getBuilder("inf");
//            triggerMode = triggerModeBuilder.build(new DomainConfig());
//        } else {
//            triggerMode = TriggerModeFactory.getInstance().getTriggerMode(triggerModeFromConfig);
//        }
//        AbstractTrigger trigger = builder.build(config);
//        trigger.setTriggerMode(triggerMode);
//        return trigger;
//    }
//}
