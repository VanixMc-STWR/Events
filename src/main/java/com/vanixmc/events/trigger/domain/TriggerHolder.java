package com.vanixmc.events.trigger.domain;

import java.util.ArrayList;
import java.util.List;

public class TriggerHolder {
    private final List<Trigger> triggers;

    public TriggerHolder() {
        this.triggers = new ArrayList<>();
    }

    public TriggerHolder(List<Trigger> triggers) {
        this.triggers = new ArrayList<>(triggers);
    }

    public void populate(TriggerHolder triggerHolder) {
        this.triggers.addAll(triggerHolder.triggers);
    }

}
