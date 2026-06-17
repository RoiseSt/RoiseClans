package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.List;

public class ClanTopCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanTopCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan top {kills/level}");
            return;
        }

        try {
            String sortBy = args[0].toLowerCase();
            if (!"kills".equals(sortBy) && !"level".equals(sortBy)) {
                MessageUtil.sendError(player, "Сортировать по: kills или level");
                return;
            }

            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            List<Clan> topClans = repo.getTopClans(sortBy, 10);

            MessageUtil.sendInfo(player, "&8=== &aТоп 10 кланов по " + sortBy.toUpperCase() + " &8===");
            int position = 1;
            for (Clan clan : topClans) {
                MessageUtil.sendInfo(player, "&a#" + position + " &7" + clan.getName() + " &8- &7" + 
                    ("kills".equals(sortBy) ? clan.getKills() : clan.getLevel()));
                position++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
