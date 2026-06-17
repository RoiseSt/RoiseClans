package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanHelpCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanHelpCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        MessageUtil.sendInfo(player, "&8=== &aRiseClans Help &8===");
        MessageUtil.sendInfo(player, "&a/clan help &8- Shows this message");
        MessageUtil.sendInfo(player, "&a/clan create {name} &8- Create a clan");
        MessageUtil.sendInfo(player, "&a/clan delete &8- Delete your clan");
        MessageUtil.sendInfo(player, "&a/clan info {clan} &8- Get clan info");
        MessageUtil.sendInfo(player, "&a/clan invite {player} &8- Invite player to clan");
        MessageUtil.sendInfo(player, "&a/clan accept {clan} &8- Accept invite");
        MessageUtil.sendInfo(player, "&a/clan decline {clan} &8- Decline invite");
        MessageUtil.sendInfo(player, "&a/clan kick {player} &8- Kick player from clan");
        MessageUtil.sendInfo(player, "&a/clan leave &8- Leave your clan");
        MessageUtil.sendInfo(player, "&a/clan member {player} &8- Check player info");
        MessageUtil.sendInfo(player, "&a/clan pvp &8- Toggle PvP mode");
        MessageUtil.sendInfo(player, "&a/clan top {kills/level} &8- Top clans");
        MessageUtil.sendInfo(player, "&a/clan chat {message} &8- Send to clan chat");
        MessageUtil.sendInfo(player, "&a/clan setZam {player} &8- Set vice leader");
        MessageUtil.sendInfo(player, "&a/clan removeZam &8- Remove vice leader");
        MessageUtil.sendInfo(player, "&a/clan setLeader {player} &8- Change leader");
    }
}