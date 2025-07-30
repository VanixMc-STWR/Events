package com.vanixmc.events.action;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class ActionConfig {
    private final Map<String, Object> config;

    public ActionConfig() {
        config = new HashMap<>();
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

    public Boolean getBoolean(String key) {
        Object value = config.get(key);
        return value instanceof Boolean ? (Boolean) value : null;
    }
}
