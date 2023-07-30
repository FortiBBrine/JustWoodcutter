package me.fortibrine.woodcutter.inventory;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class SellInventory implements InventoryHolder {

    private Inventory inventory;
    private Map<Material, Double> costOfBlocks;
    private MessageManager messageManager;
    private EconomyManager economyManager;
    private WoodcutterAxe axe;
    private BoosterManager boosterManager;

    public SellInventory(Woodcutter plugin, Player player) {
        VariableManager variableManager = plugin.getVariableManager();
        FileConfiguration config = plugin.getConfig();

        this.axe = plugin.getVariableManager().getWoodcutterAxeList().get(plugin.getSqlManager().getAxeLevel(player.getUniqueId().toString()));
        this.costOfBlocks = variableManager.getCostOfBlocks();
        this.messageManager = plugin.getMessageManager();
        this.economyManager = plugin.getEconomyManager();
        this.boosterManager = plugin.getBoosterManager();

        inventory = Bukkit.createInventory(
                this,
                27,
                messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("sell-inventory.title"))).replace("&", "§")
        );

        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("sell-inventory.item.material")));

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("sell-inventory.item.name"))).replace("&", "§"));

        List<String> lore = config.getStringList("sell-inventory.item.lore");
        lore.replaceAll(s -> messageManager.supportMessagesJSON(messageManager.supportColorsHEX(s)).replace("&", "§"));
        meta.setLore(lore);

        item.setItemMeta(meta);

        inventory.setItem(13, item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);

        if (event.getSlot() != 13) return;

        Inventory playerInventory = player.getInventory();

        double amount = 0;

        for (ItemStack item : playerInventory.getContents()) {
            Material material = item.getType();

            amount += item.getAmount() * costOfBlocks.getOrDefault(material, 0.0);

            playerInventory.remove(item);
        }

        amount *= axe.getBooster();
        amount *= this.boosterManager.getLocalBooster(player.getUniqueId());

        economyManager.giveMoney(player, amount);

        String message = messageManager.parseString("sell").replace("%cost", String.valueOf(amount));

        player.sendMessage(message);
    }
}
