package com.vanixmc.events.shared;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface ConfigBuilder<T> {
    T build(DomainConfig domainConfig);

    /**
     * Recursively resolves inheritance for conditions by merging parent and child configurations.
     * If the condition has an "id" key, it inherits from the parent condition with that id.
     * Child properties override parent properties.
     *
     * @param key The key to resolve
     * @param map The map of all objects from file
     * @return The merged DomainConfig
     */
    static DomainConfig resolveConfig(String key, Map<String, Map<String, Object>> map) {
        Map<String, Object> raw = new HashMap<>(map.get(key));
        if (raw.containsKey("id")) {
            // Inherit from parent condition
            String parentId = (String) raw.get("id");
            DomainConfig parent = resolveConfig(parentId, map);
            Map<String, Object> parentMap = new HashMap<>(parent.getConfig());
            parentMap.putAll(raw); // Child overrides parent
            DomainConfig merged = new DomainConfig();
            merged.getConfig().putAll(parentMap);
            return merged;
        } else {
            // No inheritance, just use raw config
            DomainConfig config = new DomainConfig();
            config.getConfig().putAll(raw);
            return config;
        }
    }

}
