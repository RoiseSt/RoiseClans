package ru.roisest.riseclan.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

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
            Optional<Clan> clanOpt = repo.getClanByMember(player.getUniqueId());
            
            switch (identifier) {
                case "pvp_status":
                    if (clanOpt.isPresent()) {
                        Clan clan = clanOpt.get();
                        return clan.isPvpEnabled() ? "&aВключен" : "&cВыключен";
                    }
                    return "&7Нет";
                    
                case "clan_name":
                    if (clanOpt.isPresent()) {
                        return clanOpt.get().getName();
                    }
                    return "&7Нет";
                    
                case "clan_kills":
                    if (clanOpt.isPresent()) {
                        return String.valueOf(clanOpt.get().getKills());
                    }
                    return "0";
                    
                case "clan_deaths":
                    if (clanOpt.isPresent()) {
                        return String.valueOf(clanOpt.get().getDeaths());
                    }
                    return "0";
                    
                case "clan_level":
                    if (clanOpt.isPresent()) {
                        return String.valueOf(clanOpt.get().getLevel());
                    }
                    return "0";
                    
                case "clan_name_colored":
                    if (clanOpt.isPresent()) {
                        Clan clan = clanOpt.get();
                        return MessageUtil.translate(clan.getColor() + clan.getName());
                    }
                    return MessageUtil.translate("&7Нет");
                    
                case "clan_color":
                    if (clanOpt.isPresent()) {
                        return clanOpt.get().getColor();
                    }
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
