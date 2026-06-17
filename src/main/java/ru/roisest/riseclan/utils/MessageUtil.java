package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;

public class MessageUtil {
    private static String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', 
            RiseClans.getInstance().getConfig().getString("messages.prefix", "&8[&aRiseClans&8] &r"));
    }

    public static void sendInfo(Player player, String message) {
        player.sendMessage(getPrefix() + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendSuccess(Player player, String message) {
        player.sendMessage(getPrefix() + ChatColor.GREEN + message);
    }

    public static void sendError(Player player, String message) {
        player.sendMessage(getPrefix() + ChatColor.RED + message);
    }

    public static void sendWarning(Player player, String message) {
        player.sendMessage(getPrefix() + ChatColor.YELLOW + message);
    }

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}