package me.fortibrine.woodcutter.utils;

import lombok.Getter;
import me.fortibrine.woodcutter.Woodcutter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class VariableManager {

    private List<WoodcutterAxe> woodcutterAxeList = new ArrayList<>();
    private Map<Material, Double> costOfBlocks = new HashMap<>();
    private Map<Material, Material> blocks = new HashMap<>();
    private Map<Block, Material> regenerateBlocks = new HashMap<>();

    public VariableManager(Woodcutter plugin) {

        FileConfiguration config = plugin.getConfig();

        int level = 0;
        for (String key : config.getConfigurationSection("axes").getKeys(false)) {
            ConfigurationSection configurationSection = config.getConfigurationSection("axes." + key);

            ItemStack item = new ItemStack(Material.matchMaterial(configurationSection.getString("item.material")));

            double booster = configurationSection.getDouble("booster");
            double cost = configurationSection.getDouble("cost");

            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(configurationSection.getString("item.name").replace("%booster", String.valueOf(booster)).replace("%level", String.valueOf(level)));

            final int finalLevel = level;

            List<String> lore = configurationSection.getStringList("item.lore");
            lore.replaceAll(s -> s.replace("%booster", String.valueOf(booster)).replace("%level", String.valueOf(finalLevel)));
            meta.setLore(lore);

            item.setItemMeta(meta);

            WoodcutterAxe axe = new WoodcutterAxe(level, booster, item, cost);

            woodcutterAxeList.add(axe);

            level++;
        }

        for (String key : config.getConfigurationSection("blocks").getKeys(false)) {
            double cost = config.getDouble("blocks." + key);

            this.costOfBlocks.put(Material.matchMaterial(key), cost);

        }

        for (String key : config.getConfigurationSection("settings.blocks").getKeys(false)) {
            Material material = Material.matchMaterial(key);
            Material giveMaterial = Material.matchMaterial(config.getString("settings.blocks." + key));

            this.blocks.put(material, giveMaterial);
        }
    }

}
