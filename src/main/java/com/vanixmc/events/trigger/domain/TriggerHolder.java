package com.vanixmc.events.trigger.domain;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class TriggerHolder {
    private final List<Trigger> triggerList;

    public TriggerHolder() {
        this.triggerList = new ArrayList<>();
    }

    public TriggerHolder(List<Trigger> triggers) {
        this.triggerList = new ArrayList<>(triggers);
    }

    public void resubscribeAll(Triggerable triggerable) {
        triggerList.forEach(trigger -> trigger.subscribe(triggerable));
    }

    public void unsubscribeAll(Triggerable triggerable) {
        triggerList.forEach(trigger -> trigger.unsubscribe(triggerable));
    }

    public void populate(TriggerHolder triggerHolder) {
        this.triggerList.addAll(triggerHolder.triggerList);
    }

    public boolean isEmpty() {
        return triggerList.isEmpty();
    }
}