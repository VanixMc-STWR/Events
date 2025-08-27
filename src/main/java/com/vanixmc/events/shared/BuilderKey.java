package com.vanixmc.events.shared;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class BuilderKey {
    private static final Map<String, BuilderKey> REGISTRY = new ConcurrentHashMap<>();

    private BuilderKey(Set<String> keys) {
        // register all names/aliases
        for (String key : keys) {
            REGISTRY.put(key.toLowerCase(), this);
        }
    }

    public static BuilderKey of(String... keys) {
        return new BuilderKey(Set.of(keys));
    }

    public static BuilderKey from(String key) {
        BuilderKey result = REGISTRY.get(key.toLowerCase());
        if (result == null) {
            throw new IllegalArgumentException("Unknown BuilderKey: " + key);
        }
        return result;
    }
}