package com.vanixmc.events.action;

import java.util.ArrayList;
import java.util.List;

public class ActionHolder {
    private final List<EventAction> actions;

    public ActionHolder(EventAction... eventActions) {
        this.actions = new ArrayList<>(List.of(eventActions));
    }
}
