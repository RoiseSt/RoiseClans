package ru.roisest.riseclan.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;

public class ClanPlaceholder extends PlaceholderExpansion {
    private RiseClans plugin;

    public ClanPlaceholder(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "RoiseSt";
    }

    @Override
    public String getIdentifier() {
        return "riseclans";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return null;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            switch (identifier) {
                case "pvp_status":
                    return "Unknown";
                case "clan_name":
                    return "None";
                case "clan_kills":
                    return "0";
                case "clan_deaths":
                    return "0";
                case "clan_level":
                    return "0";
                case "clan_name_colored":
                    return "&7None";
                case "clan_color":
                    return "&7";
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}