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

public class ClanKickCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanKickCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan kick {игрок}");
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());

            // Проверяем, что отправитель — лидер клана
            Optional<Clan> clanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendError(player, "Вы не являетесь лидером клана");
                return;
            }

            Clan clan = clanOpt.get();

            String targetName = args[0];
            // Сначала пытаемся найти онлайн игрока
            Player targetOnline = Bukkit.getPlayerExact(targetName);
            java.util.UUID targetUUID = null;
            String targetDisplayName = targetName;

            if (targetOnline != null) {
                targetUUID = targetOnline.getUniqueId();
                targetDisplayName = targetOnline.getName();
            } else {
                // Ищем в членах клана по имени (чтобы поддерживать оффлайн-кик)
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
                MessageUtil.sendError(player, "Игрок не найден в вашем клане");
                return;
            }

            // Нельзя кикать самого себя
            if (targetUUID.equals(player.getUniqueId())) {
                MessageUtil.sendError(player, "Вы не можете кикнуть себя");
                return;
            }

            // Нельзя кикать лидера
            if (clan.getLeaderUUID() != null && clan.getLeaderUUID().equals(targetUUID)) {
                MessageUtil.sendError(player, "Вы не можете кикнуть лидера клана");
                return;
            }

            // Проверяем, что цель действительно состоит в клане (на случай несоответствий)
            Optional<Clan> targetClanOpt = repo.getClanByMember(targetUUID);
            if (!targetClanOpt.isPresent() || targetClanOpt.get().getId() != clan.getId()) {
                MessageUtil.sendError(player, "Этот игрок не состоит в вашем клане");
                return;
            }

            // Удаляем участника
            repo.removeMember(clan.getId(), targetUUID);

            MessageUtil.sendSuccess(player, "Игрок " + targetDisplayName + " был исключён из клана");

            // Уведомляем цель, если онлайн
            OfflinePlayer offline = Bukkit.getOfflinePlayer(targetUUID);
            if (offline != null && offline.isOnline()) {
                Player online = offline.getPlayer();
                if (online != null) {
                    MessageUtil.sendError(online, "Вы были исключены из клана \"" + clan.getName() + "\"");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
