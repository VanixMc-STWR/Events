package com.vanixmc.events.trigger.triggers;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.TickTime;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

public class RecurringTrigger extends AbstractTrigger {
    private final TickTime interval;
    private @Nullable
    final TickTime delay;

    private BukkitTask bukkitTask;

    public RecurringTrigger(TickTime interval, @Nullable TickTime delay) {
        this.interval = interval;
        this.delay = delay;
    }

    @Override
    public void register() {
        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                fire(Context.builder().build());
            }
        }.runTaskTimer(EventsPlugin.getInstance(),
                delay != null ? delay.getTickValue() : 0,
                interval.getTickValue());
    }

    @Override
    public void unregister() {
        if (bukkitTask == null) return;
        bukkitTask.cancel();
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            TickTime interval = config.parseTime("interval");
            TickTime delay = config.parseTime("delay");

            return new RecurringTrigger(interval, delay);
        };
    }
}
