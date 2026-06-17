package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanInviteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInviteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Usage: /clan invite {player}");
            return;
        }
        MessageUtil.sendError(player, "Coming soon");
    }
}