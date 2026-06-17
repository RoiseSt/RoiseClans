package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanMemberCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanMemberCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan member {игрок}");
            return;
        }
        MessageUtil.sendError(player, "Скоро будет доступно");
    }
}
