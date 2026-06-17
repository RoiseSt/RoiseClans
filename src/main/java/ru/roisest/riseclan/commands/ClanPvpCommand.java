package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import ru.roisest.riseclan.database.ClanRepository;
import java.sql.SQLException;
import java.util.Optional;

public class ClanPvpCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanPvpCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository clanRepo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = getClanByPlayer(player, clanRepo);
            if (!clanOpt.isPresent()) {
                MessageUtil.sendError(player, "You are not in a clan!");
                return;
            }
            
            Clan clan = clanOpt.get();
            
            if (!clan.getLeaderUUID().equals(player.getUniqueId()) && 
                (clan.getViceLeaderUUID() == null || !clan.getViceLeaderUUID().equals(player.getUniqueId()))) {
                MessageUtil.sendError(player, "Only clan leader or vice leader can toggle PVP!");
                return;
            }
            
            boolean newPvpState = !clan.isPvpEnabled();
            clan.setPvpEnabled(newPvpState);
            clanRepo.updateClan(clan);
            
            String status = newPvpState ? "enabled" : "disabled";
            MessageUtil.sendSuccess(player, "PVP between clan members has been " + status + "!");
            
        } catch (SQLException e) {
            MessageUtil.sendError(player, "Database error!");
            plugin.getLogger().severe("Error toggling PVP: " + e.getMessage());
        }
    }
    
    private Optional<Clan> getClanByPlayer(Player player, ClanRepository clanRepo) throws SQLException {
        return Optional.empty();
    }
}
