package com.vanixmc.events.event.registry;

import com.vanixmc.events.event.domain.AbstractEvent;
import com.vanixmc.events.event.domain.EventBuilder;
import com.vanixmc.events.event.domain.EventType;
import com.vanixmc.events.files.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class EventRegistry {
    private final Map<EventType, EventBuilder> eventBuilderMap;
    private final Map<String, AbstractEvent> eventMap;
    private final FileManager fileManager;

    public EventRegistry(Map<EventType, EventBuilder> eventBuilderMap) {
        this.eventBuilderMap = new HashMap<>();
        this.eventMap = new HashMap<>();
        this.fileManager = new FileManager("events");
    }

    public void register(String id, AbstractEvent event) {
        eventMap.put(id, event);
    }

    public void registerBuilders() {
    }

    public void loadAllEvents() {
        for (String fileName : fileManager.listFileNames()) {
            FileConfiguration config = fileManager.load(fileName);
            // Deserialize your event here using config
        }
    }

    public void saveEvent(String eventId, FileConfiguration config) {
        fileManager.load(eventId).setDefaults(config);
        fileManager.save(eventId);
    }

    public FileConfiguration getEventConfig(String eventId) {
        return fileManager.getConfig(eventId);
    }
}
