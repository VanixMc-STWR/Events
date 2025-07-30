package com.vanixmc.events.action;

import com.vanixmc.events.event.EventContext;

public interface Action {
    void execute(EventContext context);
}