package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.Map;

public class ClanDeleteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanDeleteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Clan clan = clanOpt.get();
            
            repo.deleteClanMembers(clan.getId());
            repo.deleteClan(clan.getId());
            
            MessageUtil.sendFromConfig(player, "clan-deleted", Map.of("clan", clan.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
