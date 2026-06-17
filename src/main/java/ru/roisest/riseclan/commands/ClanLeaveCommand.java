package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

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
                MessageUtil.sendError(player, "Вы не состоите ни в каком клане");
                return;
            }
            
            Clan clan = clanOpt.get();
            
            if (clan.getLeaderUUID().equals(player.getUniqueId())) {
                MessageUtil.sendError(player, "Лидер клана не может покинуть клан. Передайте роль лидера другому участнику или удалите клан");
                return;
            }
            
            repo.removeMember(clan.getId(), player.getUniqueId());
            MessageUtil.sendSuccess(player, "Вы покинули клан");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
