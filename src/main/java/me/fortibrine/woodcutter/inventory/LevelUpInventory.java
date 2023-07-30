package me.fortibrine.woodcutter.inventory;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.utils.EconomyManager;
import me.fortibrine.woodcutter.utils.MessageManager;
import me.fortibrine.woodcutter.utils.SQLManager;
import me.fortibrine.woodcutter.utils.WoodcutterAxe;
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

public class LevelUpInventory implements InventoryHolder {

    private SQLManager sqlManager;
    private MessageManager messageManager;
    private EconomyManager economyManager;
    private List<WoodcutterAxe> woodcutterAxeList;
    private ItemStack currentItem;

    private Inventory inventory;
    public LevelUpInventory(Woodcutter plugin, Player player) {
        FileConfiguration config = plugin.getConfig();


        int level = sqlManager.getAxeLevel(player.getUniqueId().toString());

        this.woodcutterAxeList = plugin.getVariableManager().getWoodcutterAxeList();
        this.currentItem = woodcutterAxeList.get(level).getItem();
        this.sqlManager = plugin.getSqlManager();
        this.messageManager = plugin.getMessageManager();
        this.economyManager = plugin.getEconomyManager();

        WoodcutterAxe axe = this.woodcutterAxeList.get(level + 1);

        inventory = Bukkit.createInventory(
                this,
                27,
                messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("level-inventory.title"))).replace("&", "§")
        );

        ItemStack item = new ItemStack(Material.matchMaterial(config.getString("level-inventory.item.material")));

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(
                messageManager.supportMessagesJSON(messageManager.supportColorsHEX(config.getString("level-inventory.item.name")))
                        .replace("&", "§")
                        .replace("%cost", String.valueOf(axe.getCost()))
                        .replace("%booster", String.valueOf(axe.getBooster()))
                        .replace("%level", String.valueOf(axe.getLevel()))
        );

        List<String> lore = config.getStringList("level-inventory.item.lore");
        lore.replaceAll(
                s -> messageManager.supportMessagesJSON(messageManager.supportColorsHEX(s))
                        .replace("&", "§")
                        .replace("%cost", String.valueOf(axe.getCost()))
                        .replace("%booster", String.valueOf(axe.getBooster()))
                        .replace("%level", String.valueOf(axe.getLevel()))
        );
        meta.setLore(lore);

        item.setItemMeta(meta);

        inventory.setItem(12, woodcutterAxeList.get(level).getItem());
        inventory.setItem(14, item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Inventory playerInventory = player.getInventory();

        switch (event.getSlot()) {
            case 12:
                playerInventory.addItem(this.currentItem);
                break;
            case 14:
                int level = sqlManager.getAxeLevel(player.getUniqueId().toString());

                if (level >= this.woodcutterAxeList.size()) return;

                WoodcutterAxe axe = this.woodcutterAxeList.get(level + 1);

                double cost = axe.getCost();

                if (!economyManager.takeMoney(player, cost)) {
                    this.messageManager.sendMessage(player, "not-enough-money");
                    return;
                }

                sqlManager.levelUp(player.getUniqueId().toString());

                playerInventory.remove(this.currentItem);
                playerInventory.addItem(axe.getItem());

                break;
        }
    }
}
