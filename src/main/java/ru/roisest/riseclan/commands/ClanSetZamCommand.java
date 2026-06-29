package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.Map;

public class ClanSetZamCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanSetZamCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendFromConfig(player, "no-permission", null);
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Clan clan = clanOpt.get();
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageUtil.sendFromConfig(player, "player-not-found", Map.of("player", args[0]));
                return;
            }
            
            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (!targetClan.isPresent() || targetClan.get().getId() != clan.getId()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            if (clan.getViceLeaderUUID() != null) {
                repo.updateMemberRole(clan.getId(), clan.getViceLeaderUUID(), "MEMBER");
            }
            
            clan.setViceLeaderUUID(targetPlayer.getUniqueId());
            repo.updateClan(clan);
            repo.updateMemberRole(clan.getId(), targetPlayer.getUniqueId(), "VICE_LEADER");
            
            MessageUtil.sendFromConfig(player, "setzam-success", Map.of("player", targetPlayer.getName()));
            MessageUtil.sendFromConfig(targetPlayer, "setzam-target", Map.of("clan", clan.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
