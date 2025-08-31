package com.vanixmc.events.action.core_actions;

import com.vanixmc.events.action.domain.AbstractAction;
import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
public class GiveItemAction extends AbstractAction {
    private final ItemStack item;

    @Override
    public boolean execute(Context context) {
        Player player = context.getPlayer() != null ? context.getPlayer() :
                context.getEntity() instanceof Player player1 ? player1 : null;
        if (player == null) {
            Chat.log("NULL!");
            return false;
        }

        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
        if (leftovers.isEmpty()) return false;

        dropLeftovers(player, leftovers);
        return false;
    }

    public static ConfigBuilder<AbstractAction> builder() {
        return config -> {
            String materialName = config.getString("material");
            if (materialName == null || materialName.isBlank()) {
                throw new IllegalArgumentException("Missing or empty 'material' for GiveItemAction");
            }
            Material material = Material.matchMaterial(materialName); // case-insensitive, supports namespaced keys
            if (material == null) {
                throw new IllegalArgumentException("Invalid material: " + materialName);
            }

            Integer amountValue = config.getInt("amount");
            int amount = (amountValue != null && amountValue > 0) ? amountValue : 1;

            String rawName = config.getString("name");
            String coloredName = (rawName == null || rawName.isBlank()) ? null : Chat.colorize(rawName);

            List<String> rawLore = config.getStringList("lore");
            List<String> coloredLore = (rawLore == null || rawLore.isEmpty())
                    ? Collections.emptyList()
                    : Chat.colorizeList(rawLore);

            ItemStack stack = new ItemStack(material, amount);
            ItemMeta meta = stack.getItemMeta();
            if (meta == null) {
                throw new IllegalStateException("Could not get ItemMeta for " + stack);
            }

            if (coloredName != null) {
                meta.setDisplayName(coloredName);
            }
            if (!coloredLore.isEmpty()) {
                meta.setLore(coloredLore);
            }

            stack.setItemMeta(meta);

            return new GiveItemAction(stack);
        };
    }

    private void dropLeftovers(Player player, Map<Integer, ItemStack> leftovers) {
        leftovers.values().forEach(stack ->
                player.getWorld().dropItemNaturally(player.getLocation(), stack)
        );
    }
}