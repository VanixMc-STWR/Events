package com.vanixmc.events;

import com.vanixmc.events.action.impl.command_action.CommandAction;
import com.vanixmc.events.action.impl.command_action.CommandSender;
import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.action.factory.ActionFactory;
import com.vanixmc.events.action.impl.message_action.MessageFormat;
import com.vanixmc.events.action.impl.message_action.MessageAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class ActionLoadTest {
    private ActionFactory actionFactory;

    @BeforeEach
    public void initFactory() {
        this.actionFactory = new ActionFactory();
    }

    @Test
    public void testReusableActionsLoadCorrectly() {
        Yaml yaml = new Yaml();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("actions.yml");
        Map<String, Object> data = yaml.load(inputStream);
        assertNotNull(data);
        assertTrue(data.containsKey("actions"));
        Map<String, Map<String, Object>> actions = (Map<String, Map<String, Object>>) data.get("actions");

        actionFactory.registerAll(actions);

        List<Action> actionsList = new ArrayList<>();
        for (String key : actions.keySet()) {
            Action action = actionFactory.create(key, actions);
            actionsList.add(action);
            System.out.println(action);
        }
        System.out.println(actionsList);
        System.out.println(actionFactory.getRegistry());

        for (int i = 0; i < actionsList.size(); i++) {
            if (i == 0) {
                assertInstanceOf(MessageAction.class, actionsList.get(i));
                MessageAction action = (MessageAction) actionsList.get(i);
                assertEquals(MessageFormat.CHAT, action.getFormat());
                assertEquals("Hello World!", action.getMessage());
            } else if (i == 1) {
                assertInstanceOf(MessageAction.class, actionsList.get(i));
                MessageAction action = (MessageAction) actionsList.get(i);
                assertEquals(MessageFormat.CHAT, action.getFormat());
                assertEquals("Hello, Rare World!!", action.getMessage());
            } else if (i == 2) {
                assertInstanceOf(MessageAction.class, actionsList.get(i));
                MessageAction action = (MessageAction) actionsList.get(i);
                assertEquals(MessageFormat.CHAT, action.getFormat());
                assertEquals("Hello, Epic World!!!", action.getMessage());
            } else if (i == 3) {
                assertInstanceOf(CommandAction.class, actionsList.get(i));
                CommandAction action = (CommandAction) actionsList.get(i);
                assertEquals(CommandSender.PLAYER, action.getCommandSender());
                assertEquals("gmc", action.getCommand());
            }
        }
    }
}
