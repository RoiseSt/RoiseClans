package ru.roisest.riseclan.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.UUID;

public class ClanMemberCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanMemberCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan member {игрок}");
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            UUID targetUUID;
            String targetName;
            
            if (targetPlayer != null) {
                targetUUID = targetPlayer.getUniqueId();
                targetName = targetPlayer.getName();
            } else {
                targetUUID = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
                targetName = args[0];
            }
            
            Optional<Clan> clanOpt = repo.getClanByMember(targetUUID);
            if (!clanOpt.isPresent()) {
                MessageUtil.sendError(player, "Этот игрок не состоит ни в каком клане");
                return;
            }
            
            Clan clan = clanOpt.get();
            Optional<ClanMember> memberOpt = repo.getMember(clan.getId(), targetUUID);
            
            if (!memberOpt.isPresent()) {
                MessageUtil.sendError(player, "Игрок не найден в клане");
                return;
            }
            
            ClanMember member = memberOpt.get();
            MessageUtil.sendInfo(player, "&8=== &a" + targetName + " Информация &8===");
            MessageUtil.sendInfo(player, "&aКлан: &7" + clan.getName());
            MessageUtil.sendInfo(player, "&aРоль: &7" + member.getRole());
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
