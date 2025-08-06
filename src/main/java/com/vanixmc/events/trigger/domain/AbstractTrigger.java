package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// TODO: Make trigger mode not count failed triggerable.trigger
public abstract class AbstractTrigger implements Trigger {
    private final List<Triggerable> subscribers;
    private final TriggerMode triggerMode;
    private final String id;

    public AbstractTrigger(TriggerMode triggerMode, String id) {
        this.triggerMode = triggerMode;
        this.id = id;
        this.subscribers = new ArrayList<>();
    }

    @Nullable
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void subscribe(Triggerable triggerable) {
        if (subscribers.isEmpty()) register();
        subscribers.add(triggerable);
    }

    @Override
    public void unsubscribe(Triggerable triggerable) {
        subscribers.remove(triggerable);
    }

    @Override
    public void fire(EventContext context) {
        subscribers.forEach(triggerable -> {
            EventContext newContext = EventContext.from(context, null, null, triggerable, null);
            triggerable.trigger(newContext);
        });
        triggerMode.evaluate(this, subscribers);
    }

    @Override
    public List<Triggerable> getSubscribers() {
        return subscribers;
    }
}