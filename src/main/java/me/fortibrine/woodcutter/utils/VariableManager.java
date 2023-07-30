package me.fortibrine.woodcutter.utils;

import lombok.Getter;
import me.fortibrine.woodcutter.Woodcutter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableManager {

    @Getter
    private List<WoodcutterAxe> woodcutterAxeList = new ArrayList<>();

    @Getter
    private Map<Material, Double> costOfBlocks = new HashMap<>();

    private Woodcutter plugin;

    public VariableManager(Woodcutter plugin) {
        this.plugin = plugin;

        FileConfiguration config = this.plugin.getConfig();

        int level = 0;
        for (String key : config.getConfigurationSection("axes").getKeys(false)) {
            ConfigurationSection configurationSection = config.getConfigurationSection("aexs." + key);

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

    }

}
