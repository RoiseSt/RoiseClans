package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;
import java.util.Map;

public class ClanLeaveCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanLeaveCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = repo.getClanByMember(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Clan clan = clanOpt.get();
            
            if (clan.getLeaderUUID().equals(player.getUniqueId())) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            repo.removeMember(clan.getId(), player.getUniqueId());
            MessageUtil.sendFromConfig(player, "left-clan", Map.of("clan", clan.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
