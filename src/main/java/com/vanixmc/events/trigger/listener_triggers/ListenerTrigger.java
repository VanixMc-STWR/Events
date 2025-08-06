package com.vanixmc.events.trigger.listener_triggers;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.trigger_modes.TriggerMode;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerTrigger extends AbstractTrigger implements Listener {

    public ListenerTrigger(String id, TriggerMode triggerMode) {
        super(triggerMode, id);
    }

    @Override
    public void register() {
        EventsPlugin eventsPlugin = EventsPlugin.getInstance();
        eventsPlugin.getServer()
                .getPluginManager()
                .registerEvents(this, eventsPlugin);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
