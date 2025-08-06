package com.vanixmc.events.action.core_actions;

import com.vanixmc.events.action.domain.Action;
import com.vanixmc.events.event.domain.EventContext;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.util.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
@Getter
@ToString
@AllArgsConstructor
public class GiveItemAction implements Action {
    private final Material material;
    private final int amount;
    private final String name;
    private final List<String> lore;

    @Override
    public void execute(EventContext context) {
        Player player = context.player();
        if (player == null) throw new RuntimeException("The player is null in this context.");

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (name != null) meta.setDisplayName(Chat.colorize(name));
            if (lore != null) meta.setLore(Chat.colorizeList(lore));
            item.setItemMeta(meta);
        }
        player.getInventory().addItem(item);

    }

    public static ConfigBuilder<Action> builder() {
        return config -> {
            String materialName = config.getUppercaseString("material");
            Integer amountValue = config.getInt("amount");
            int Amount = amountValue != null ? amountValue : 1;
            String Name = config.getString("name");
            List<String> Lore = config.getStringList("lore");

            Material material = Material.getMaterial(materialName);
            if (material == null) {
                throw new IllegalArgumentException("Invalid Material" + materialName);
            }
            return new GiveItemAction(material, Amount, Name, Lore);
        };
    }
}