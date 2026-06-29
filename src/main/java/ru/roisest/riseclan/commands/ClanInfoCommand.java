package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;

import java.util.List;
import java.util.Optional;
import java.util.Map;

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
                    MessageUtil.sendFromConfig(player, "not-in-clan", null);
                    return;
                }
                clan = playerClanOpt.get();
            } else {
                Optional<Clan> clanOpt = repo.getClanByName(args[0]);
                if (!clanOpt.isPresent()) {
                    MessageUtil.sendFromConfig(player, "player-not-found-general", null);
                    return;
                }
                clan = clanOpt.get();
            }

            if (clan != null) {
                List<ClanMember> members = repo.getClanMembers(clan.getId());
                int maxMembers = plugin.getConfig().getInt("clan.max-members", 50);

                MessageUtil.sendFromConfig(player, "info-header", Map.of("clan", clan.getName()));
                MessageUtil.sendFromConfig(player, "info-level", Map.of("level", String.valueOf(clan.getLevel())));
                MessageUtil.sendFromConfig(player, "info-kills", Map.of("kills", String.valueOf(clan.getKills())));
                MessageUtil.sendFromConfig(player, "info-deaths", Map.of("deaths", String.valueOf(clan.getDeaths())));

                MessageUtil.sendFromConfig(player, "info-members", Map.of(
                    "count", String.valueOf(members.size()),
                    "max", String.valueOf(maxMembers)
                ));

                StringBuilder names = new StringBuilder();
                for (ClanMember m : members) {
                    names.append(m.getPlayerName()).append(", ");
                }
                String playerList = names.length() > 2 ? names.substring(0, names.length() - 2) : "—";

                MessageUtil.sendFromConfig(player, "info-players", Map.of("players", playerList));
                MessageUtil.sendFromConfig(player, "info-pvp", Map.of("state", clan.isPvpEnabled() ? "Включен" : "Выключен"));
                MessageUtil.sendFromConfig(player, "info-footer", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
