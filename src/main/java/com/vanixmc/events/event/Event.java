package com.vanixmc.events.event;

import com.vanixmc.events.action.ActionHolder;
import com.vanixmc.events.condition.ConditionHolder;

public interface Event {
    String getId();
    ConditionHolder getConditionHolder();
    ActionHolder getActionHolder();
    boolean start();
    boolean stop();
    boolean isRunning();
}