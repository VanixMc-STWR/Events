package com.vanixmc.events.trigger.triggers.listener_triggers.block_triggers;

import com.vanixmc.events.context.Context;
import com.vanixmc.events.shared.ConfigBuilder;
import com.vanixmc.events.trigger.domain.AbstractTrigger;
import com.vanixmc.events.trigger.triggers.listener_triggers.ListenerTrigger;
import com.vanixmc.events.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Optional;

public class BlockPlaceTrigger extends ListenerTrigger {

    private final Material blockType;

    public BlockPlaceTrigger(Material blockType) {
        this.blockType = blockType;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (blockType != event.getBlockPlaced().getType()) {

            Bukkit.getLogger().info("Not the right block.");

            return;
        }

        fire(Context.builder()
                .player(event.getPlayer())
                .bukkitEvent(event)
                .build());
    }

    public static ConfigBuilder<AbstractTrigger> builder() {

        return config -> {

            String blockTypeString = config.getUppercaseString("blockType");

            Optional<Material> material = Optional.ofNullable(Material.getMaterial(blockTypeString));

            if (material.isEmpty()) {

                Chat.log("Invalid block type: " + blockTypeString);

                return null;

            }

            if (!material.get().isBlock()) {

                Chat.log("Invalid block type: " + blockTypeString + " does not have a block.");

                return null;
            }

            return new BlockPlaceTrigger(material.get());

        };
    }
}
