package me.fortibrine.woodcutter.commands;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.inventory.SellInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSell implements CommandExecutor {

    private Woodcutter plugin;
    public CommandSell(Woodcutter plugin) {
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

        SellInventory sellInventory = new SellInventory(plugin);
        player.openInventory(sellInventory.getInventory());

        return true;
    }
}
