package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
    public void fire(Context context) {
        List<Triggerable> subscribersClone = new ArrayList<>(subscribers);
        subscribersClone.forEach(triggerable -> {
            // Only proceed if the triggerable is still in the subscribers list
            if (subscribers.contains(triggerable)) {
                context.setTriggerable(triggerable);
                boolean triggered = triggerable.trigger(context);
                if (!triggered) return;

                // Use synchronized block when evaluating to prevent concurrent modification
                synchronized (subscribers) {
                    triggerMode.evaluate(this, triggerable);
                }
            }
        });
    }

    @Override
    public List<Triggerable> getSubscribers() {
        return subscribers;
    }
}