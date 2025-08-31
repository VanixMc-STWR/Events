package com.vanixmc.events.trigger.triggers.listener_triggers;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerTrigger extends AbstractTrigger implements Listener {

    public ListenerTrigger(String id) {
        super(id);
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
        // Unregisters listener so that it no longer fires
        HandlerList.unregisterAll(this);
    }
}
