package me.fortibrine.woodcutter.commands;

import me.fortibrine.woodcutter.Woodcutter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandLevelUp implements CommandExecutor {

    private Woodcutter plugin;
    public CommandLevelUp(Woodcutter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return false;
    }
}
