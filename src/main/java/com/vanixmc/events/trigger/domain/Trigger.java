package com.vanixmc.events.trigger.domain;

import com.vanixmc.events.context.Context;

import javax.annotation.Nullable;
import java.util.List;

public interface Trigger {
    @Nullable
    String getId();

    void register();

    void unregister();

    void subscribe(Triggerable triggerable);

    void unsubscribe(Triggerable triggerable);

    void fire(Context context);

    List<Triggerable> getSubscribers();
}
