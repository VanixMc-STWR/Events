package com.vanixmc.events.action.core_actions.money_action;

import com.vanixmc.events.EventsPlugin;
import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class BankOperationAction extends AbstractAction {
    private final BankOperation bankOperation;
    private final @Nullable String bankName;
    private final double amount;

    private final Economy economy;

    public BankOperationAction(BankOperation bankOperation, @Nullable String bankName, double amount, Economy economy) {
        this.bankOperation = bankOperation;
        this.bankName = bankName;
        this.amount = amount;
        this.economy = economy;
    }

    @Override
    public boolean execute(Context context) {
        Player player = getPlayerFromContext(context);
        if (economy == null) return false;

        switch (bankOperation) {
            case DEPOSIT -> deposit(player);
            case WITHDRAW -> withdraw(player, false);
            case SET -> setBalance(player);
            default -> {
                return false;
            }
        }
        return true;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            Double amount = config.getDouble("amount");
            if (amount == null) {
                throw new IllegalArgumentException("'amount' cannot be null or empty for Money Action.");
            }
            String operation = config.getUppercaseString("operation");
            BankOperation bankOperation = BankOperation.valueOf(operation);

            String bankNameString = config.getString("bank-name");
            String bankName = !bankNameString.isEmpty() && !bankNameString.isBlank() ? bankNameString : null;

            return new BankOperationAction(bankOperation, bankName, amount, EventsPlugin.getEconomy());
        };
    }

    private void deposit(Player player) {
        if (bankName == null) {
            economy.depositPlayer(player, amount);
            return;
        }
        EconomyResponse response = bankDeposit(player);
        if (response.type == EconomyResponse.ResponseType.FAILURE) {
            createBank(player);
            bankDeposit(player);
        }
    }

    private void withdraw(Player player, boolean all) {
        if (bankName == null) {
            economy.withdrawPlayer(player, amount);
            return;
        }
        bankWithdraw(player, all);
    }

    private void setBalance(Player player) {
        if (bankName == null) {
            economy.withdrawPlayer(player, economy.getBalance(player));
            economy.depositPlayer(player, amount);
            return;
        }

        String bankId = getBankId(player);
        double currentBalance = economy.bankBalance(bankId).balance;

        if (currentBalance > 0) {
            economy.bankWithdraw(bankId, currentBalance);
        }
        economy.bankDeposit(bankId, amount);
    }

    private EconomyResponse bankDeposit(Player player) {
        return economy.bankDeposit(getBankId(player), amount);
    }

    private void bankWithdraw(Player player, boolean all) {
        String bankId = getBankId(player);
        double withdrawAmount = all ? economy.bankBalance(bankId).balance : amount;
        economy.bankWithdraw(bankId, withdrawAmount);
    }

    private void createBank(Player player) {
        economy.createBank(getBankId(player), player);
    }

    private String getBankId(Player player) {
        return player.getUniqueId().getLeastSignificantBits() + "_" + bankName;
    }
}
