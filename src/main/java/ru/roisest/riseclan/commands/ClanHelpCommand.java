package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanHelpCommand implements IClanCommand {

    private final RiseClans plugin;

    public ClanHelpCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("");

        player.sendMessage(MessageUtil.translate("&b⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ &3[&b RoiseClans &3] &b⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

        player.sendMessage("");

        player.sendMessage(MessageUtil.translate("&b/clan help &8- &fПоказывает список команд"));
        player.sendMessage(MessageUtil.translate("&b/clan create {имя} &8- &fСоздать клан"));
        player.sendMessage(MessageUtil.translate("&b/clan delete &8- &fУдалить клан"));
        player.sendMessage(MessageUtil.translate("&b/clan info {клан} &8- &fИнформация о клане"));
        player.sendMessage(MessageUtil.translate("&b/clan invite {игрок} &8- &fПригласить игрока в клан"));
        player.sendMessage(MessageUtil.translate("&b/clan accept {клан} &8- &fПринять запрос в клан"));
        player.sendMessage(MessageUtil.translate("&b/clan decline {клан} &8- &fОтклонить запрос в клан"));
        player.sendMessage(MessageUtil.translate("&b/clan kick {игрок} &8- &fКикнуть игрока"));
        player.sendMessage(MessageUtil.translate("&b/clan leave &8- &fПокинуть клан"));
        player.sendMessage(MessageUtil.translate("&b/clan member {игрок} &8- &fПосмотреть информацию о игроке"));
        player.sendMessage(MessageUtil.translate("&b/clan pvp &8- &fИзменить режим PvP в клане"));
        player.sendMessage(MessageUtil.translate("&b/clan top {kills/level} &8- &fТоп кланов"));
        player.sendMessage(MessageUtil.translate("&b/clan chat {сообщение} &8- &fНаписать в клановый чат"));
        player.sendMessage(MessageUtil.translate("&b/clan setZam {игрок} &8- &fУстановить заместителя клана"));
        player.sendMessage(MessageUtil.translate("&b/clan removeZam &8- &fУбрать заместителя клана"));
        player.sendMessage(MessageUtil.translate("&b/clan setLeader {игрок} &8- &fУстановить создателя клана"));

        player.sendMessage("");

        player.sendMessage(MessageUtil.translate("&b⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

        player.sendMessage("");
    }
}