package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {
    
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern RGB_PATTERN = Pattern.compile("<rgb:([A-Fa-f0-9]{6})>");
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:([A-Fa-f0-9]{6}):([A-Fa-f0-9]{6})>(.+?)</gradient>");
    
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
        
        String prefix = RiseClans.getInstance().getConfig().getString("messages.prefix", "");
        String raw = RiseClans.getInstance().getConfig().getString("messages." + key);
        if (raw == null) return;
        
        if (placeholders != null) {
            for (Map.Entry<String, String> e : placeholders.entrySet()) {
                raw = raw.replace("{" + e.getKey() + "}", e.getValue() == null ? "" : e.getValue());
            }
        }
        
        player.sendMessage(translate(prefix + raw));
    }
    
    public static String getConfigString(String key) {
        if (RiseClans.getInstance() == null) return null;
        return RiseClans.getInstance().getConfig().getString("messages." + key);
    }

    public static List<String> getConfigStringList(String key) {
        if (RiseClans.getInstance() == null) return java.util.Collections.emptyList();
        return RiseClans.getInstance().getConfig().getStringList("messages." + key);
    }
    
    public static String translate(String message) {
        if (message == null) return "";
        
        // Преобразуем градиенты <gradient:#XXXXXX:#XXXXXX>text</gradient>
        message = translateGradients(message);
        
        // Преобразуем RGB коды <rgb:#XXXXXX>
        Matcher rgbMatcher = RGB_PATTERN.matcher(message);
        StringBuffer rgbSb = new StringBuffer();
        while (rgbMatcher.find()) {
            String hexColor = rgbMatcher.group(1);
            String replacement = "§x§" + String.join("§", hexColor.split(""));
            rgbMatcher.appendReplacement(rgbSb, Matcher.quoteReplacement(replacement));
        }
        rgbMatcher.appendTail(rgbSb);
        message = rgbSb.toString();
        
        // Преобразуем HEX коды типа &#A9BBF8 в формат Bukkit
        Matcher hexMatcher = HEX_PATTERN.matcher(message);
        StringBuffer hexSb = new StringBuffer();
        while (hexMatcher.find()) {
            String hexColor = hexMatcher.group(1);
            String replacement = "§x§" + String.join("§", hexColor.split(""));
            hexMatcher.appendReplacement(hexSb, Matcher.quoteReplacement(replacement));
        }
        hexMatcher.appendTail(hexSb);
        message = hexSb.toString();
        
        // Преобразуем обычные & коды
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    private static String translateGradients(String message) {
        Matcher matcher = GRADIENT_PATTERN.matcher(message);
        StringBuffer sb = new StringBuffer();
        
        while (matcher.find()) {
            String startColor = matcher.group(1);
            String endColor = matcher.group(2);
            String text = matcher.group(3);
            
            String gradientText = applyGradient(text, startColor, endColor);
            matcher.appendReplacement(sb, Matcher.quoteReplacement(gradientText));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
    private static String applyGradient(String text, String startHex, String endHex) {
        if (text.isEmpty()) return text;
        
        int[] startRGB = hexToRGB(startHex);
        int[] endRGB = hexToRGB(endHex);
        
        StringBuilder result = new StringBuilder();
        int length = text.length();
        
        for (int i = 0; i < length; i++) {
            // Вычисляем процент прогресса
            float progress = length > 1 ? (float) i / (length - 1) : 0f;
            
            // Интерполируем цвета
            int r = (int) (startRGB[0] + (endRGB[0] - startRGB[0]) * progress);
            int g = (int) (startRGB[1] + (endRGB[1] - startRGB[1]) * progress);
            int b = (int) (startRGB[2] + (endRGB[2] - startRGB[2]) * progress);
            
            // Преобразуем в hex формат Bukkit
            String hexColor = String.format("%02X%02X%02X", r, g, b);
            String colorCode = "§x§" + String.join("§", hexColor.split(""));
            
            result.append(colorCode).append(text.charAt(i));
        }
        
        return result.toString();
    }
    
    private static int[] hexToRGB(String hex) {
        return new int[] {
            Integer.parseInt(hex.substring(0, 2), 16),
            Integer.parseInt(hex.substring(2, 4), 16),
            Integer.parseInt(hex.substring(4, 6), 16)
        };
    }
}
