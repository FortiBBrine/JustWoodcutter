package me.fortibrine.woodcutter.listeners;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.inventory.BoostersInventory;
import me.fortibrine.woodcutter.inventory.LevelUpInventory;
import me.fortibrine.woodcutter.inventory.SellInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements org.bukkit.event.Listener {

    private Woodcutter plugin;
    public InventoryListener(Woodcutter plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();

        if (inventory == null) return;
        if (inventory.getHolder() == null) return;

        if (inventory.getHolder() instanceof SellInventory) {
            ((SellInventory) inventory.getHolder()).onInventoryClick(event);
        }
        if (inventory.getHolder() instanceof LevelUpInventory) {
            ((LevelUpInventory) inventory.getHolder()).onInventoryClick(event);
        }
        if (inventory.getHolder() instanceof BoostersInventory) {
            ((BoostersInventory) inventory.getHolder()).onInventoryClick(event);
        }

    }

}
