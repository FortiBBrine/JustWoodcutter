package me.fortibrine.woodcutter.commands;

import me.fortibrine.woodcutter.Woodcutter;
import me.fortibrine.woodcutter.inventory.BoostersInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandBoosters implements CommandExecutor {

    private Woodcutter plugin;
    public CommandBoosters(Woodcutter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            if (args.length > 0 && args[0].equals("add")) {

                if (args.length < 5) {
                    return false;
                }

                if (!sender.hasPermission("woodcutter.boosters.add")) {
                    sender.sendMessage(plugin.getMessageManager().parseString("not-permission"));
                    return true;
                }

                UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                double booster;
                try {
                    booster = Double.parseDouble(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(plugin.getMessageManager().parseString("booster-parse-error"));

                    return true;
                }

                long seconds;
                try {
                    seconds = Long.parseLong(args[3]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(plugin.getMessageManager().parseString("long-parse-error"));

                    return true;
                }
                boolean global = Boolean.parseBoolean(args[4]);

                plugin.getSqlManager().addBooster(uuid.toString(), seconds, booster, global);
                sender.sendMessage(plugin.getMessageManager().parseString("booster-added"));

                return true;
            }
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("woodcutter.boosters")) {
            plugin.getMessageManager().sendMessage(player, "not-permission");
            return true;
        }

        if (args.length > 0 && args[0].equals("add")) {

            if (args.length < 5) {
                return false;
            }

            if (!player.hasPermission("woodcutter.boosters.add")) {
                plugin.getMessageManager().sendMessage(player, "not-permission");
                return true;
            }

            UUID uuid = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
            double booster;
            try {
                booster = Double.parseDouble(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.getMessageManager().parseString("booster-parse-error"));

                return true;
            }

            long seconds;
            try {
                seconds = Long.parseLong(args[3]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(plugin.getMessageManager().parseString("long-parse-error"));

                return true;
            }
            boolean global = Boolean.parseBoolean(args[4]);

            plugin.getSqlManager().addBooster(uuid.toString(), seconds, booster, global);
            plugin.getMessageManager().sendMessage(player, "booster-added");

            return true;
        }

        BoostersInventory boostersInventory = new BoostersInventory(plugin, player.getUniqueId());
        player.openInventory(boostersInventory.getInventory());

        return true;
    }
}
