package com.vanixmc.events.action;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ActionMeta {
    @Getter
    private final ActionType type;
    private final ActionBuilder builder;

    public Action create(ActionConfig config) {
        return builder.build(config);
    }
}
