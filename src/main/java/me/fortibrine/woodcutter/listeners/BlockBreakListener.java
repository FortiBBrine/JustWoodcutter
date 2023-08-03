package me.fortibrine.woodcutter.listeners;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.utils.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    private Woodcutter plugin;
    private FileConfiguration config;
    private VariableManager variableManager;
    public BlockBreakListener(Woodcutter plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.variableManager = plugin.getVariableManager();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        if (!config.getBoolean("settings.handle-block-break")) return;
        event.setCancelled(true);

        Block block = event.getBlock();
        Material blockMaterial = block.getType();

        if (!variableManager.getBlocks().containsKey(blockMaterial)) return;

        Material giveMaterial = variableManager.getBlocks().get(blockMaterial);

        block.setType(Material.AIR);
        player.getInventory().addItem(new ItemStack(giveMaterial));

        variableManager.getRegenerateBlocks().put(block, blockMaterial);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            block.setType(blockMaterial);
            variableManager.getRegenerateBlocks().remove(block);
        }, config.getInt("settings.regenerate-time") * 20L);

    }

}
