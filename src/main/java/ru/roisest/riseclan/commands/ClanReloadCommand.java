package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanReloadCommand implements IClanCommand {

    private final RiseClans plugin;

    public ClanReloadCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("riseclans.reload")) {
            MessageUtil.sendError(player, "У вас нет прав на эту команду!");
            return;
        }

        plugin.reloadConfig();
        MessageUtil.sendSuccess(player, "Конфиг успешно перезагружен!");
    }
}
