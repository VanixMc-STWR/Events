package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.event.domain.EventContext;

import javax.annotation.Nullable;
import java.util.List;

public interface Trigger {
    @Nullable
    String getId();

    void register();

    void unregister();

    void subscribe(Triggerable triggerable);

    void unsubscribe(Triggerable triggerable);

    void fire(EventContext context);

    List<Triggerable> getSubscribers();
}
