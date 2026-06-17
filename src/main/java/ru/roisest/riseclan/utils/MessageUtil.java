package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    
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
    
    public static String translate(String message) {
        // Преобразуем HEX коды типа &#A9BBF8 в формат Bukkit
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            String replacement = "§x§" + String.join("§", hexColor.split(""));
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        
        // Преобразуем обычные & коды
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}
