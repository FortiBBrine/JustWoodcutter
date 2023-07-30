package me.fortibrine.woodcutter.commands;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.inventory.LevelUpInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLevelUp implements CommandExecutor {

    private Woodcutter plugin;
    public CommandLevelUp(Woodcutter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("woodcutter.sell")) {
            plugin.getMessageManager().sendMessage(player, "not-permission");
            return true;
        }

        LevelUpInventory levelUpInventory = new LevelUpInventory(plugin, player);
        player.openInventory(levelUpInventory.getInventory());

        return true;
    }
}
