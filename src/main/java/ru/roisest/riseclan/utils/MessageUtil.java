package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {
    
    public static void sendSuccess(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + message));
    }
    
    public static void sendError(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + message));
    }
    
    public static void sendInfo(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + message));
    }
    
    public static void sendChat(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
