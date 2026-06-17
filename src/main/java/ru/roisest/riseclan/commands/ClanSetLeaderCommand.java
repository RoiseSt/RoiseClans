package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanSetLeaderCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanSetLeaderCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Usage: /clan setLeader {player}");
            return;
        }
        MessageUtil.sendError(player, "Coming soon");
    }
}