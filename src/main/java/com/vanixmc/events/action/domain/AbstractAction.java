package com.vanixmc.events.action.domain;

import com.vanixmc.events.event.domain.EventContext;

public abstract class AbstractAction implements Action {

    @Override
    public boolean trigger(EventContext context) {
        return execute(context);
    }

}
