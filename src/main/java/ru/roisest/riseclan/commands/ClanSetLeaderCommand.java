package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

public class ClanSetLeaderCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanSetLeaderCommand(RiseClans plugin) {
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
            
            UUID oldLeaderUUID = clan.getLeaderUUID();
            clan.setLeaderUUID(targetPlayer.getUniqueId());
            clan.setViceLeaderUUID(null);
            repo.updateClan(clan);
            
            repo.updateMemberRole(clan.getId(), targetPlayer.getUniqueId(), "LEADER");
            repo.updateMemberRole(clan.getId(), oldLeaderUUID, "MEMBER");
            
            MessageUtil.sendFromConfig(player, "setleader-success", Map.of("player", targetPlayer.getName()));
            MessageUtil.sendFromConfig(targetPlayer, "setleader-target", Map.of("clan", clan.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
