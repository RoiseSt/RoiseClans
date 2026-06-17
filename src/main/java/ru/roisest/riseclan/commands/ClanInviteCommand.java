package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

public class ClanInviteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInviteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan invite {игрок}");
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> playerClanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!playerClanOpt.isPresent()) {
                playerClanOpt = repo.getClanByMemberRole(player.getUniqueId(), "VICE_LEADER");
            }
            if (!playerClanOpt.isPresent()) {
                MessageUtil.sendError(player, "Вы не в клане или не имеете прав на приглашение");
                return;
            }
            
            Clan clan = playerClanOpt.get();
            
            if (!clan.getLeaderUUID().equals(player.getUniqueId()) && 
                (clan.getViceLeaderUUID() == null || !clan.getViceLeaderUUID().equals(player.getUniqueId()))) {
                MessageUtil.sendError(player, "Только лидер или заместитель могут приглашать игроков");
                return;
            }
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageUtil.sendError(player, "Игрок не найден");
                return;
            }
            
            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                MessageUtil.sendError(player, "Вы не можете пригласить себя");
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (targetClan.isPresent()) {
                MessageUtil.sendError(player, "Этот игрок уже состоит в клане");
                return;
            }
            
            repo.createInvitation(clan.getId(), targetPlayer.getUniqueId(), targetPlayer.getName());
            
            MessageUtil.sendSuccess(player, "Приглашение отправлено игроку " + targetPlayer.getName());
            MessageUtil.sendInfo(targetPlayer, "Вам отправлено приглашение в клан \"" + clan.getName() + "\". Используйте /clan accept");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
