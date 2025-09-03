package com.vanixmc.events.action.core_actions.money_action;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class BankOperationAction extends AbstractAction {
    private final BankOperation bankOperation;
    private final double amount;

    public BankOperationAction(BankOperation bankOperation, double amount) {
        this.bankOperation = bankOperation;
        this.amount = amount;
    }

    @Override
    public boolean execute(Context context) {
        Player player = context.getPlayer();
        if (player == null) return false;

        Economy economy = EventsPlugin.getEconomy();
        switch (bankOperation) {
            case DEPOSIT -> {
                economy.depositPlayer(player, amount);
                return true;
            }
            case WITHDRAW -> {
                economy.withdrawPlayer(player, amount);
                return true;
            }
            case SET -> {
                // Withdraw all money
                economy.withdrawPlayer(player, economy.getBalance(player));

                // Deposit designated amount
                economy.depositPlayer(player, amount);
                return true;
            }
        }
        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            Double amount = config.getDouble("amount");
            if (amount == null) {
                throw new IllegalArgumentException("'amount' cannot be null or empty for Money Action.");
            }
            String operation = config.getUppercaseString("operation");
            BankOperation bankOperation = BankOperation.valueOf(operation);

            return new BankOperationAction(bankOperation, amount);
        };
    }
}
