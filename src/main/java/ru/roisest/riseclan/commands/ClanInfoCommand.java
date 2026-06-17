package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
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
                MessageUtil.sendInfo(player, "&8=== &a" + clan.getName() + " Информация &8===");
                MessageUtil.sendInfo(player, "&aУровень: &7" + clan.getLevel());
                MessageUtil.sendInfo(player, "&aУбийств: &7" + clan.getKills());
                MessageUtil.sendInfo(player, "&aСмертей: &7" + clan.getDeaths());
                MessageUtil.sendInfo(player, "&aСоотношение У/С: &7" + String.format("%.2f", clan.getKDRatio()));
                MessageUtil.sendInfo(player, "&aПвП: &7" + (clan.isPvpEnabled() ? "Включен" : "Выключен"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
