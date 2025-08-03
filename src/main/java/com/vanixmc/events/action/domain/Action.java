package com.vanixmc.events.action.domain;

import com.vanixmc.events.event.domain.EventContext;

public interface Action {
    void execute(EventContext context);
}