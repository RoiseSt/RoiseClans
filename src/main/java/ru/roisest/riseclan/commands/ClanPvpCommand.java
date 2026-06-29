package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import ru.roisest.riseclan.database.ClanRepository;
import java.sql.SQLException;
import java.util.Map;
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
            Optional<Clan> clanOpt = clanRepo.getClanByMember(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }

            Clan clan = clanOpt.get();

            if (!clan.getLeaderUUID().equals(player.getUniqueId()) &&
                (clan.getViceLeaderUUID() == null || !clan.getViceLeaderUUID().equals(player.getUniqueId()))) {
                MessageUtil.sendFromConfig(player, "pvp-no-permission", null);
                return;
            }

            boolean newPvpState = !clan.isPvpEnabled();
            clan.setPvpEnabled(newPvpState);
            clanRepo.updateClan(clan);

            Map<String, String> ph = Map.of("state", newPvpState ? "&#00FF00включен" : "&#FF5555выключен");
            MessageUtil.sendFromConfig(player, "pvp-toggled", ph);

        } catch (SQLException e) {
            MessageUtil.sendFromConfig(player, "error-db", null);
            plugin.getLogger().severe("Error toggling PVP: " + e.getMessage());
        }
    }
}
