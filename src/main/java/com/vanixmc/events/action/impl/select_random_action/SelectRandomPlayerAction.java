package com.vanixmc.events.action.impl.select_random_action;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SelectRandomPlayerAction extends AbstractAction {
    private final String variableKey;

    public SelectRandomPlayerAction(String variableKey) {
        this.variableKey = variableKey;
    }

    @Override
    public boolean execute(Context context) {
        List<Player> online = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (online.isEmpty()) return false;
        Player choice = online.get(ThreadLocalRandom.current().nextInt(online.size()));
        context.getEvent().getPersistentData().addContext(variableKey, choice.getName());
        return true;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String variableKey = config.getString("stored-as");

            return new SelectRandomPlayerAction(variableKey);
        };
    }
}
