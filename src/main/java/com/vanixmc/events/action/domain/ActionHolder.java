package com.vanixmc.events.action.domain;

import com.vanixmc.events.event.domain.EventContext;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class ActionHolder {
    private final List<Action> actions;

    public ActionHolder(Action... actions) {
        this.actions = new ArrayList<>(List.of(actions));
    }

    public ActionHolder(List<Action> actions) {
        this.actions = new ArrayList<>(actions);
    }

    public void executeAll(EventContext eventContext) {
        actions.forEach(action -> action.execute(eventContext));
    }
}
