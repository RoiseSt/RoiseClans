package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

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
                MessageUtil.sendError(player, "Вы не являетесь лидером клана");
                return;
            }
            
            Clan clan = clanOpt.get();
            
            repo.deleteClanMembers(clan.getId());
            repo.deleteClan(clan.getId());
            
            MessageUtil.sendSuccess(player, "Клан \"" + clan.getName() + "\" успешно удален");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка при удалении клана");
        }
    }
}
