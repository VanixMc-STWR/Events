package com.vanixmc.events.trigger.non_listener_triggers;
import com.ibm.icu.util.TimeUnitAmount;
import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.shared.TickTime;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RecurringTrigger extends AbstractTrigger {

    private final TickTime interval;
    private final TickTime delay;
    private final int repetitions;

    private BukkitTask bukkitTask;

    public RecurringTrigger(TickTime interval, TickTime delay,
                            int repetitions) {
        this.interval = interval;
        this.delay = delay;
        this.repetitions = repetitions;
    }

    @Override
    public void register() {
        bukkitTask = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                fire(Context.builder().build());

                if (repetitions == -1) return;

                ++count;

                if (count >= repetitions) unregister();
            }
        }.runTaskTimer(EventsPlugin.getInstance(),
                delay.getTickValue(), interval.getTickValue());
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

            int repetitions = config.parseRepetitions();

            return new RecurringTrigger(interval, delay, repetitions);
        };
    }
}
