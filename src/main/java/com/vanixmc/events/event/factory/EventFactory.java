package com.vanixmc.events.event.factory;

import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.event.domain.EventType;
import com.vanixmc.events.event.zone_event.ZoneEvent;
import com.vanixmc.events.files.FileManager;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.DomainConfig;
import com.vanixmc.events.util.Chat;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EventFactory {
    private final Map<EventType, ConfigBuilder<Event>> builders;
    private final Map<String, Event> eventMap;
    private final FileManager fileManager;

    public EventFactory() {
        this.builders = new HashMap<>();
        this.eventMap = new HashMap<>();
        this.fileManager = new FileManager("events");
        registerBuilders();
        loadAllEvents();
    }

    public void register(String id, AbstractEvent event) {
        eventMap.put(id, event);
    }

    public void registerBuilders() {
        builders.put(EventType.ZONE, ZoneEvent.builder());
    }

    public void loadAllEvents() {
        for (String fileName : fileManager.listFileNames()) {
            Map<String, Object> data = fileManager.load(fileName);
            DomainConfig config = new DomainConfig(data);
            config.getConfig().put("id", fileName);

            EventType type = EventType.valueOf(config.getUppercaseString("type"));
            ConfigBuilder<Event> builder = builders.get(type);

            if (builder == null) {
                throw new IllegalArgumentException("Unknown action type: " + type);
            }
            Event event = builder.build(config);
            eventMap.put(fileName, event);
            Chat.log("Loaded event: " + fileName);
        }
    }

//    public void saveEvent(String eventId, FileConfiguration config) {
//        fileManager.load(eventId).setDefaults(config);
//        fileManager.save(eventId);
//    }

    public FileConfiguration getEventConfig(String eventId) {
        return fileManager.getConfig(eventId);
    }
}
