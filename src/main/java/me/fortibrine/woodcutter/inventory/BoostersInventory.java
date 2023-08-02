package me.fortibrine.woodcutter.inventory;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.utils.Booster;
import me.fortibrine.woodcutter.utils.MessageManager;
import me.fortibrine.woodcutter.utils.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BoostersInventory implements InventoryHolder {

    private Inventory inventory;
    private SQLManager sqlManager;
    private MessageManager messageManager;
    private Map<ItemStack, Booster> items = new HashMap<>();

    public BoostersInventory(Woodcutter plugin, UUID uuid) {
        FileConfiguration config = plugin.getConfig();

        this.sqlManager = plugin.getSqlManager();
        this.messageManager = plugin.getMessageManager();

        inventory = Bukkit.createInventory(this, 54, messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("boosters-inventory.title")))
                        .replace("&", "§"));

        List<Booster> boosters = this.sqlManager.getBoosters(uuid.toString());

        boosters.forEach(booster -> {
            ItemStack item;
            if (booster.isGlobal()) {
                item = new ItemStack(Material.matchMaterial(config.getString("boosters-inventory.global.material")));

                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(
                        messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("boosters-inventory.global.name")))
                                .replace("&", "§")
                                .replace("%booster", String.valueOf(booster.getBooster()))
                                .replace("%time", String.valueOf(booster.getTime()))
                );

                List<String> lore = config.getStringList("boosters-inventory.global.lore");
                lore.replaceAll(s -> messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("booster-inventory.global.name")))
                        .replace("&", "§")
                        .replace("%booster", String.valueOf(booster.getBooster()))
                        .replace("%time", String.valueOf(booster.getTime())));
                meta.setLore(lore);

                item.setItemMeta(meta);

            } else {
                item = new ItemStack(Material.matchMaterial(config.getString("boosters-inventory.local.material")));

                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(
                        messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("boosters-inventory.local.name")))
                                .replace("&", "§")
                                .replace("%booster", String.valueOf(booster.getBooster()))
                                .replace("%time", String.valueOf(booster.getTime()))
                );

                List<String> lore = config.getStringList("boosters-inventory.local.lore");
                lore.replaceAll(s -> messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("booster-inventory.global.name")))
                        .replace("&", "§")
                        .replace("%booster", String.valueOf(booster.getBooster()))
                        .replace("%time", String.valueOf(booster.getTime())));
                meta.setLore(lore);

                item.setItemMeta(meta);
            }
            inventory.addItem(item);
            items.put(item, booster);
        });

    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
