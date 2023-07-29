package me.fortibrine.woodcutter.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.fortibrine.woodcutter.Woodcutter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.bukkit.ChatColor.COLOR_CHAR;

public class MessageManager {

    private YamlConfiguration messagesConfig;
    public MessageManager(Woodcutter plugin) {
        this.messagesConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder() + File.separator + "messages.yml"));
    }

    public void sendMessage(Player player, String path) {
        if (!this.messagesConfig.contains(path)) {
            return;
        }

        String message = this.messagesConfig.getString(path);

        message = supportMessagesJSON(supportColorsHEX(message)).replace("&", "§");
        message = PlaceholderAPI.setPlaceholders(player, message);

        player.sendMessage(message);
    }

    public String parseString(String path) {
        String message = this.messagesConfig.getString(path);

        message = supportMessagesJSON(supportColorsHEX(message)).replace("&", "§");

        return message;
    }

    public List<String> parseStringList(String path) {
        List<String> stringList = this.messagesConfig.getStringList(path);

        stringList.replaceAll(message -> supportMessagesJSON(supportColorsHEX(message)).replace("&", "§"));

        return stringList;
    }

    public String supportColorsHEX(String nameMessage) {

        String sversion;

        try {
            sversion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            return nameMessage;
        }

        if (Integer.parseInt(sversion.split("\\_")[1]) < 16) {
            return nameMessage;
        }

        final Pattern hexColorsPattern = Pattern.compile("&#([a-f0-9]{6})");
        final Matcher matcherPattern = hexColorsPattern.matcher(nameMessage);
        StringBuffer buffer = new StringBuffer(nameMessage.length() + 4 * 8);
        while (matcherPattern.find())
        {
            String group = matcherPattern.group(1);
            matcherPattern.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcherPattern.appendTail(buffer).toString();
    }

    public String supportMessagesJSON(String nameMessage) {

        if (nameMessage.startsWith("[json]")) {
            return new TextComponent(ComponentSerializer.parse(nameMessage.substring(5))).toString();
        }
        return nameMessage;
    }

}