package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.Map;

public class ClanAcceptCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanAcceptCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Integer> invitationOpt = repo.getLatestInvitation(player.getUniqueId());
            if (!invitationOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-invitation", null);
                return;
            }
            
            int clanId = invitationOpt.get();
            Optional<Clan> clanOpt = repo.getClanById(clanId);
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }
            
            Clan clan = clanOpt.get();
            
            Optional<Clan> playerClan = repo.getClanByMember(player.getUniqueId());
            if (playerClan.isPresent()) {
                MessageUtil.sendFromConfig(player, "already-in-clan", null);
                return;
            }
            
            int maxMembers = plugin.getConfig().getInt("clan.max-members", 50);
            if (repo.getClanMembers(clan.getId()).size() >= maxMembers) {
                MessageUtil.sendFromConfig(player, "clan-full", Map.of("max", String.valueOf(maxMembers)));
                return;
            }
            
            ClanMember member = new ClanMember();
            member.setPlayerUUID(player.getUniqueId());
            member.setPlayerName(player.getName());
            member.setRole("MEMBER");
            
            repo.addMember(clan.getId(), member);
            repo.deleteInvitation(player.getUniqueId());
            
            MessageUtil.sendFromConfig(player, "joined-clan", Map.of("clan", clan.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
