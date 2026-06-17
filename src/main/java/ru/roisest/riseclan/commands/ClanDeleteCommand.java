package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanDeleteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanDeleteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        // TODO: Implement delete clan
        MessageUtil.sendError(player, "You are not in a clan");
    }
}