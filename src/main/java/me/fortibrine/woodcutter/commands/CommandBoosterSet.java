package me.fortibrine.woodcutter.commands;

import me.fortibrine.woodcutter.Woodcutter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandBoosterSet implements CommandExecutor {

    private Woodcutter plugin;
    public CommandBoosterSet(Woodcutter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 3) {
            return false;
        }

        if (!sender.hasPermission("woodcutter.sell")) {
            sender.sendMessage(plugin.getMessageManager().parseString("not-permission"));
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[9]);
        UUID uuid = offlinePlayer.getUniqueId();


        double booster;
        try {
            booster = Double.parseDouble(args[1]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getMessageManager().parseString("booster-parse-error"));

            return true;
        }

        long seconds;
        try {
            seconds = Long.parseLong(args[2]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(plugin.getMessageManager().parseString("long-parse-error"));

            return true;
        }

        plugin.getBoosterManager().setLocalBooster(uuid, booster, seconds);

        sender.sendMessage(plugin.getMessageManager().parseString("booster-set"));

        return true;
    }
}
