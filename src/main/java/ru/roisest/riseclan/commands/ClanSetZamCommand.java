package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

public class ClanSetZamCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanSetZamCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan setZam {игрок}");
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendError(player, "Вы не являетесь лидером клана");
                return;
            }
            
            Clan clan = clanOpt.get();
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageUtil.sendError(player, "Игрок не найден");
                return;
            }
            
            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                MessageUtil.sendError(player, "Вы не можете сделать себя заместителем");
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (!targetClan.isPresent() || targetClan.get().getId() != clan.getId()) {
                MessageUtil.sendError(player, "Этот игрок не состоит в вашем клане");
                return;
            }
            
            if (clan.getViceLeaderUUID() != null) {
                repo.updateMemberRole(clan.getId(), clan.getViceLeaderUUID(), "MEMBER");
            }
            
            clan.setViceLeaderUUID(targetPlayer.getUniqueId());
            repo.updateClan(clan);
            repo.updateMemberRole(clan.getId(), targetPlayer.getUniqueId(), "VICE_LEADER");
            
            MessageUtil.sendSuccess(player, "Игрок " + targetPlayer.getName() + " теперь заместитель лидера");
            MessageUtil.sendSuccess(targetPlayer, "Вы стали заместителем лидера клана \"" + clan.getName() + "\"");
            MessageUtil.sendInfo(targetPlayer, "Теперь вы можете: приглашать игроков, кикать участников и переключать режим ПвП");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
