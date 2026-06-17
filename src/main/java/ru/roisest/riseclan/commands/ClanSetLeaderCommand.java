package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.UUID;

public class ClanSetLeaderCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanSetLeaderCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan setLeader {игрок}");
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
                MessageUtil.sendError(player, "Вы не можете сделать себя лидером");
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (!targetClan.isPresent() || targetClan.get().getId() != clan.getId()) {
                MessageUtil.sendError(player, "Этот игрок не состоит в вашем клане");
                return;
            }
            
            UUID oldLeaderUUID = clan.getLeaderUUID();
            clan.setLeaderUUID(targetPlayer.getUniqueId());
            clan.setViceLeaderUUID(oldLeaderUUID);
            repo.updateClan(clan);
            
            repo.updateMemberRole(clan.getId(), targetPlayer.getUniqueId(), "LEADER");
            repo.updateMemberRole(clan.getId(), oldLeaderUUID, "VICE_LEADER");
            
            MessageUtil.sendSuccess(player, "Роль лидера передана игроку " + targetPlayer.getName());
            MessageUtil.sendSuccess(targetPlayer, "Вы стали лидером клана \"" + clan.getName() + "\"");
            MessageUtil.sendInfo(player, "Вы теперь заместитель лидера");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
