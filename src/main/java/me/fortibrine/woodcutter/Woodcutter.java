package me.fortibrine.woodcutter;

import lombok.Getter;
import me.fortibrine.woodcutter.commands.CommandLevelUp;
import me.fortibrine.woodcutter.commands.CommandSell;
import me.fortibrine.woodcutter.listeners.Listener;
import me.fortibrine.woodcutter.utils.EconomyManager;
import me.fortibrine.woodcutter.utils.MessageManager;
import me.fortibrine.woodcutter.utils.SQLManager;
import me.fortibrine.woodcutter.utils.VariableManager;
import org.bukkit.Bukkit;
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

        this.getCommand("level").setExecutor(new CommandLevelUp(this));
        this.getCommand("sell").setExecutor(new CommandSell(this));

        pluginManager.registerEvents(new Listener(this), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
