package me.fortibrine.woodcutter;

import lombok.Getter;
import me.fortibrine.woodcutter.commands.CommandBoosterSet;
import me.fortibrine.woodcutter.commands.CommandBoosters;
import me.fortibrine.woodcutter.commands.CommandLevelUp;
import me.fortibrine.woodcutter.commands.CommandSell;
import me.fortibrine.woodcutter.listeners.BlockBreakListener;
import me.fortibrine.woodcutter.listeners.InventoryListener;
import me.fortibrine.woodcutter.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public final class Woodcutter extends JavaPlugin {

    @Getter
    private SQLManager sqlManager;

    @Getter
    private VariableManager variableManager;

    @Getter
    private MessageManager messageManager;

    @Getter
    private EconomyManager economyManager;

    @Getter
    private BoosterManager boosterManager;

    @Override
    public void onEnable() {

        PluginManager pluginManager = Bukkit.getPluginManager();

        if (!pluginManager.isPluginEnabled("Vault")) {
            this.getLogger().log(Level.WARNING, "Plugin can't work without Vault, sorry!");

            pluginManager.disablePlugin(this);
            return;
        }

        File config = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
            this.saveResource("messages.yml", false);
        }

        sqlManager = new SQLManager();
        variableManager = new VariableManager(this);
        messageManager = new MessageManager(this);
        economyManager = new EconomyManager();
        boosterManager = new BoosterManager();

        this.getCommand("level").setExecutor(new CommandLevelUp(this));
        this.getCommand("sell").setExecutor(new CommandSell(this));
        this.getCommand("booster").setExecutor(new CommandBoosterSet(this));
        this.getCommand("boosters").setExecutor(new CommandBoosters(this));

        pluginManager.registerEvents(new InventoryListener(this), this);
        pluginManager.registerEvents(new BlockBreakListener(this), this);

//        this.sqlManager.addBooster(Bukkit.getOfflinePlayer("IJustFortiLive").getUniqueId().toString(), 1600, 1.1, true);
    }

    @Override
    public void onDisable() {
        this.sqlManager.close();

        for (Block block : this.variableManager.getRegenerateBlocks().keySet()) {
            block.setType(variableManager.getRegenerateBlocks().get(block));
        }
    }
}
