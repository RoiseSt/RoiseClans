package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public class ClanKickCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanKickCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendFromConfig(player, "usage-kick", null);
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());

            Optional<Clan> clanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "only-leader-can-kick", null);
                return;
            }

            Clan clan = clanOpt.get();

            String targetName = args[0];
            Player targetOnline = Bukkit.getPlayerExact(targetName);
            java.util.UUID targetUUID = null;
            String targetDisplayName = targetName;

            if (targetOnline != null) {
                targetUUID = targetOnline.getUniqueId();
                targetDisplayName = targetOnline.getName();
            } else {
                List<ClanMember> members = repo.getClanMembers(clan.getId());
                for (ClanMember m : members) {
                    if (m.getPlayerName() != null && m.getPlayerName().equalsIgnoreCase(targetName)) {
                        targetUUID = m.getPlayerUUID();
                        targetDisplayName = m.getPlayerName();
                        break;
                    }
                }
            }

            if (targetUUID == null) {
                MessageUtil.sendFromConfig(player, "player-not-found", Map.of("player", targetName));
                return;
            }

            if (targetUUID.equals(player.getUniqueId())) {
                MessageUtil.sendFromConfig(player, "cannot-kick-self", null);
                return;
            }

            if (clan.getLeaderUUID() != null && clan.getLeaderUUID().equals(targetUUID)) {
                MessageUtil.sendFromConfig(player, "cannot-kick-leader", null);
                return;
            }

            Optional<Clan> targetClanOpt = repo.getClanByMember(targetUUID);
            if (!targetClanOpt.isPresent() || targetClanOpt.get().getId() != clan.getId()) {
                MessageUtil.sendFromConfig(player, "kick-not-in-clan", null);
                return;
            }

            repo.removeMember(clan.getId(), targetUUID);

            MessageUtil.sendFromConfig(player, "kick-success", 
                Map.of("player", targetDisplayName, "clan", clan.getName()));

            OfflinePlayer offline = Bukkit.getOfflinePlayer(targetUUID);
            if (offline != null && offline.isOnline()) {
                Player online = offline.getPlayer();
                if (online != null) {
                    MessageUtil.sendFromConfig(online, "kicked", Map.of("clan", clan.getName()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
