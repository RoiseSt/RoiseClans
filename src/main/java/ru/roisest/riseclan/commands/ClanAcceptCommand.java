package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

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
                MessageUtil.sendError(player, "У вас нет приглашений в клан");
                return;
            }
            
            int clanId = invitationOpt.get();
            Optional<Clan> clanOpt = repo.getClanById(clanId);
            if (!clanOpt.isPresent()) {
                MessageUtil.sendError(player, "Клан не найден");
                return;
            }
            
            Clan clan = clanOpt.get();
            
            Optional<Clan> playerClan = repo.getClanByMember(player.getUniqueId());
            if (playerClan.isPresent()) {
                MessageUtil.sendError(player, "Вы уже состоите в клане " + playerClan.get().getName());
                return;
            }
            
            ClanMember member = new ClanMember();
            member.setPlayerUUID(player.getUniqueId());
            member.setPlayerName(player.getName());
            member.setRole("MEMBER");
            
            repo.addMember(clan.getId(), member);
            repo.deleteInvitation(player.getUniqueId());
            
            MessageUtil.sendSuccess(player, "Вы присоединились к клану \"" + clan.getName() + "\"");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
