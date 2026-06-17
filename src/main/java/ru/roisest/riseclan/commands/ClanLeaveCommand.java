package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanLeaveCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanLeaveCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        MessageUtil.sendError(player, "Coming soon");
    }
}