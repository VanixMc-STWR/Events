package com.vanixmc.events.trigger.triggers.listener_triggers.block_triggers;

import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockBreakTrigger extends ListenerTrigger {

    private final Material blockType;
    @Nullable
    private final Material toolInHand;

    public BlockBreakTrigger(Material blockType, @Nullable Material toolInHand) {
        this.blockType = blockType;
        this.toolInHand = toolInHand;
    }

    private static boolean isTool(Material material) {

        String materialName = material.getData().getName();

        //  determine if the material corresponds to armor.

        if (materialName.endsWith("_HELMET") ||
            materialName.endsWith("_CHESTPLATE") ||
            materialName.endsWith("_LEGGINGS") ||
            materialName.endsWith("_BOOTS") ||
            materialName.equals("ELYTRA") ||
            materialName.equals("TURTLE_HELMET"))
        {
            return true;
        }

        //  determine if the material corresponds to an item of type Damageable.

        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(material);

        return meta instanceof Damageable;
    }

    public static ConfigBuilder<AbstractTrigger> builder() {
        return config -> {
            //  acquire block type.

            String blockParam = config.getString("block_type");

            if (blockParam.isEmpty()) {
                throw new IllegalArgumentException("Invalid 'block_type' parameter - must provide a block name.");
            }

            Optional<Material> blockType = Optional.ofNullable(Material
                    .getMaterial(blockParam.toUpperCase()));

            if (blockType.isEmpty()) {
                throw new IllegalArgumentException("Invalid 'block_type' parameter - must provide a valid block name.");
            }

            //  acquire tool type.

            String toolParam = config.getString("tool_in_hand");

            if (toolParam.isEmpty()) {
                throw new IllegalArgumentException("Invalid 'tool_in_hand' parameter - must provide a tool name.");
            }

            Optional<Material> toolType = Optional.ofNullable(Material.getMaterial(toolParam));

            if (toolType.isEmpty()) {
                throw new IllegalArgumentException("Invalid 'tool_in_hand' parameter - must provide a valid tool name.");
            }

            //  determine if mentioned material qualifies as a tool.

            if (!isTool(toolType.get())) {
                throw new IllegalArgumentException("Invalid 'tool_in_hand' parameter - must provide a valid tool.");
            }

            return new BlockBreakTrigger(blockType.get(), toolType.get());

        };
    }
}
