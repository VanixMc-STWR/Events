package com.vanixmc.events.shared;

import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.exceptions.NoBuilderDefinedException;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFactory<BT, RT> {
    protected final Map<BuilderKey, ConfigBuilder<BT>> builders;

    @Getter
    protected final Map<String, RT> registry;

    public AbstractFactory() {
        this.builders = new HashMap<>();
        this.registry = new HashMap<>();
        registerAllBuilders();
    }

    public void registerBuilder(BuilderKey builderKey, ConfigBuilder<BT> builder) {
        builders.put(builderKey, builder);
    }

    /**
     * Retrieves the action builder for the specified action type.
     *
     * @param type the type of action to get a builder for (case-sensitive)
     * @return the builder for the specified action type
     * @throws NoBuilderDefinedException if no builder is registered for the given action type
     */
    public ConfigBuilder<BT> getBuilder(String type) {
        ConfigBuilder<BT> builder = this.builders.get(BuilderKey.from(type));
        if (builder == null) {
            throw new NoBuilderDefinedException(type);
        }
        return builder;
    }

    public BT create(String key, Map<String, Map<String, Object>> fileData, Event event) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, fileData);
        String type = config.getString("type");
        ConfigBuilder<BT> builder = this.getBuilder(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }
        return builder.build(config);
    }

    public BT create(String key, Map<String, Map<String, Object>> fileData) {
        DomainConfig config = ConfigBuilder.resolveConfig(key, fileData);
        String type = config.getString("type");
        ConfigBuilder<BT> builder = this.getBuilder(type);

        if (builder == null) {
            throw new IllegalArgumentException("Unknown condition type: " + type);
        }
        return builder.build(config);
    }

    public abstract void registerAllBuilders();
}
