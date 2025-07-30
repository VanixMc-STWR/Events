package com.vanixmc.events.action;

@FunctionalInterface
public interface ActionBuilder {
    Action build(ActionConfig config);
}
