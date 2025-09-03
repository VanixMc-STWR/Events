package com.vanixmc.events.shared;

import lombok.Getter;
import lombok.ToString;
import org.bukkit.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@SuppressWarnings("unchecked | unused")
public class DomainConfig {
    private final Map<String, Object> config;

    public DomainConfig() {
        config = new HashMap<>();
    }

    public DomainConfig(Map<String, Object> data) {
        config = new HashMap<>(data);
    }

    /**
     * Retrieves the value associated with the given key as a String.
     * Returns an empty string if the value is not a String or is absent.
     *
     * @param key the key to look up
     * @return the String value, or an empty string if not found or not a String
     */
    public String getString(String key) {
        Object value = config.get(key);
        return value instanceof String ? (String) value : "";
    }

    /**
     * Retrieves the value associated with the given key as a String.
     * Returns an empty string if the value is not a String or is absent.
     *
     * @param key the key to look up
     * @return the String value, or an empty string if not found or not a String
     */
    public String getUppercaseString(String key) {
        Object value = config.get(key);
        return value instanceof String ? ((String) value).toUpperCase() : "";
    }

    public Integer getInt(String key) {
        Object value = config.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    public Long getLong(String key) {
        return (Long) config.get(key);
    }

    public Double getDouble(String key) {
        return (Double) config.get(key);
    }

    public Boolean getBoolean(String key) {
        Object value = config.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }

    public Object getObject(String key) {
        return config.get(key);
    }

    public String getId() {
        return (String) config.get("id");
    }

    public List<Object> getObjectList(String key) {
        Object value = config.get(key);
        if (value instanceof List) {
            return (List<Object>) value;
        }
        return Collections.emptyList();
    }

    public List<String> getStringList(String key) {
        Object value = config.get(key);
        if (value instanceof List<?> list) {
            boolean allStrings = list.stream().allMatch(e -> e instanceof String);
            if (allStrings) {
                @SuppressWarnings("unchecked")
                List<String> strings = (List<String>) list;
                return strings;
            }
        }
        return Collections.emptyList();
    }

    // Bukkit object methods

    public Location getLocation(String key) {
        Object value = config.get(key);
        return Location.deserialize((Map<String, Object>) value);
    }
}
