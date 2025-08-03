package com.vanixmc.events.action.domain;

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

    public ActionHolder(List<Object> objectList) {
        List<Action> actions = objectList.stream()
                .filter(o -> o instanceof Action)
                .map(o -> (Action) o)
                .toList();
        this.actions = new ArrayList<>(actions);
    }

}
