package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;

import java.util.List;
import java.util.Optional;

public class ClanInfoCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInfoCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());

            Clan clan = null;

            if (args.length == 0) {
                Optional<Clan> playerClanOpt = repo.getClanByMember(player.getUniqueId());
                if (!playerClanOpt.isPresent()) {
                    MessageUtil.sendError(player, "Вы не состоите ни в каком клане");
                    return;
                }
                clan = playerClanOpt.get();
            } else {
                Optional<Clan> clanOpt = repo.getClanByName(args[0]);
                if (!clanOpt.isPresent()) {
                    MessageUtil.sendError(player, "Клан не найден");
                    return;
                }
                clan = clanOpt.get();
            }

            if (clan != null) {
                List<ClanMember> members = repo.getClanMembers(clan.getId());
                int maxMembers = plugin.getConfig().getInt("clan.max-members", 10);

                // Header with gradient/accent color (uses MessageUtil.translate and hex code A9BBF8)
                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯ &3[ &#A9BBF8" + clan.getName() + " &3] &#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8Уровень: &7" + clan.getLevel()));
                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8Убийств: &7" + clan.getKills()));
                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8Смертей: &7" + clan.getDeaths()));

                // Members count as current/max
                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8Участники: &7" + members.size() + "/" + maxMembers));

                // Players list (names). If empty show dash
                StringBuilder names = new StringBuilder();
                for (ClanMember m : members) {
                    names.append(m.getPlayerName()).append(", ");
                }
                String playerList = names.length() > 2 ? names.substring(0, names.length() - 2) : "—";

                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8Игроки: &7" + playerList));

                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8ПвП: &7" + (clan.isPvpEnabled() ? "Включен" : "Выключен")));

                // Footer
                MessageUtil.sendInfo(player, MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
