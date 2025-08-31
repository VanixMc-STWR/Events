package com.vanixmc.events.action.domain;

import com.vanixmc.events.condition.domain.ConditionHolder;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.event.domain.Event;
import com.vanixmc.events.event.zone_event.ZoneEvent;
import com.vanixmc.events.trigger.domain.TriggerHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractAction implements Action {
    private final ConditionHolder conditionHolder;
    private final TriggerHolder triggerHolder;

    @Getter
    @Setter
    private @Nullable Event event;

    protected AbstractAction() {
        this.conditionHolder = new ConditionHolder();
        this.triggerHolder = new TriggerHolder();
    }

    @Override
    public boolean trigger(Context context) {
        return execute(context);
    }

    @Override
    public TriggerHolder getTriggerHolder() {
        return triggerHolder;
    }

    protected List<Player> getGlobalScopePlayers() {
        return event != null && event instanceof ZoneEvent zoneEvent ? zoneEvent.getPlayersInZone() :
                new ArrayList<>(Bukkit.getOnlinePlayers());
    }
}
