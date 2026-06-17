package ru.roisest.riseclan.utils;

import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class GradientUtil {
    
    private static final ChatColor[] GRADIENT_COLORS = {
        ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, 
        ChatColor.AQUA, ChatColor.BLUE, ChatColor.LIGHT_PURPLE
    };
    
    private static final ChatColor[] FORBIDDEN_COLORS = {
        ChatColor.BOLD, ChatColor.STRIKETHROUGH, ChatColor.UNDERLINE
    };
    
    public static String applyGradient(String text, ChatColor startColor, ChatColor endColor) {
        if (text == null || text.isEmpty() || isForbiddenColor(startColor) || isForbiddenColor(endColor)) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        List<ChatColor> colors = generateGradient(startColor, endColor, text.length());
        
        for (int i = 0; i < text.length(); i++) {
            result.append(colors.get(i)).append(text.charAt(i));
        }
        
        return result.toString();
    }
    
    public static String applyRainbowGradient(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            ChatColor color = GRADIENT_COLORS[i % GRADIENT_COLORS.length];
            result.append(color).append(text.charAt(i));
        }
        
        return result.toString();
    }
    
    public static String applyCustomGradient(String text, ChatColor[] colors) {
        if (text == null || text.isEmpty() || colors.length == 0) {
            return text;
        }
        
        for (ChatColor color : colors) {
            if (isForbiddenColor(color)) {
                return text;
            }
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            ChatColor color = colors[i % colors.length];
            result.append(color).append(text.charAt(i));
        }
        
        return result.toString();
    }
    
    private static List<ChatColor> generateGradient(ChatColor startColor, ChatColor endColor, int steps) {
        List<ChatColor> gradient = new ArrayList<>();
        
        ChatColor[] colors = {startColor};
        for (int i = 0; i < steps; i++) {
            gradient.add(colors[i % colors.length]);
        }
        gradient.add(endColor);
        
        return gradient;
    }
    
    public static String interpolateGradient(String text, ChatColor startColor, ChatColor endColor) {
        if (text == null || text.isEmpty() || isForbiddenColor(startColor) || isForbiddenColor(endColor)) {
            return text;
        }
        
        StringBuilder result = new StringBuilder();
        ChatColor[] transitionColors = getTransitionColors(startColor, endColor);
        
        for (int i = 0; i < text.length(); i++) {
            int colorIndex = (int) ((i / (double) text.length()) * (transitionColors.length - 1));
            result.append(transitionColors[colorIndex]).append(text.charAt(i));
        }
        
        return result.toString();
    }
    
    private static ChatColor[] getTransitionColors(ChatColor start, ChatColor end) {
        ChatColor[] colors = new ChatColor[7];
        colors[0] = start;
        colors[1] = ChatColor.GOLD;
        colors[2] = ChatColor.YELLOW;
        colors[3] = ChatColor.GREEN;
        colors[4] = ChatColor.AQUA;
        colors[5] = ChatColor.BLUE;
        colors[6] = end;
        return colors;
    }
    
    private static boolean isForbiddenColor(ChatColor color) {
        for (ChatColor forbidden : FORBIDDEN_COLORS) {
            if (color == forbidden) {
                return true;
            }
        }
        return false;
    }
}
