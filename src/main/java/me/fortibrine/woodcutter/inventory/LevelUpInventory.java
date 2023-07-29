package me.fortibrine.woodcutter.inventory;

import me.fortibrine.woodcutter.Woodcutter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class LevelUpInventory implements InventoryHolder {

    private Inventory inventory;
    public LevelUpInventory(Woodcutter plugin) {

    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {

    }
}
