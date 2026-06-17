package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

public class ClanDeclineCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanDeclineCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Integer> invitationOpt = repo.getLatestInvitation(player.getUniqueId());
            if (!invitationOpt.isPresent()) {
                MessageUtil.sendError(player, "У вас нет приглашений в клан");
                return;
            }
            
            repo.deleteInvitation(player.getUniqueId());
            MessageUtil.sendSuccess(player, "Приглашение отклонено");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
}
