package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;

import java.util.Map;
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

    // Send message from config 'messages.<key>' with optional placeholders map
    public static void sendFromConfig(Player player, String key, Map<String, String> placeholders) {
        if (RiseClans.getInstance() == null) {
            // fallback
            player.sendMessage(translate(key));
            return;
        }
        String raw = RiseClans.getInstance().getConfig().getString("messages." + key);
        if (raw == null) raw = key;
        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                raw = raw.replace("{" + e.getKey() + "}", e.getValue() == null ? "" : e.getValue());
            }
        }
        player.sendMessage(translate(raw));
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
