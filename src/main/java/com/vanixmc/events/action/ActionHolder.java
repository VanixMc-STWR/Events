package com.vanixmc.events.action;

import java.util.ArrayList;
import java.util.List;

public class ActionHolder {
    private final List<Action> actions;

    public ActionHolder(Action... actions) {
        this.actions = new ArrayList<>(List.of(actions));
    }
}
