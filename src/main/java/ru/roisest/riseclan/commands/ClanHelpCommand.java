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
        player.sendMessage(MessageUtil.translate(plugin.getConfig().getString("messages.prefix", "") + "&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯ [RoiseClans] ⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        player.sendMessage("");
        
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan help &8- &fПоказывает список команд"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan create {имя} &8- &fСоздать клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan delete &8- &fУдалить клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan info {клан} &8- &fИнформация о клане"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan invite {игрок} &8- &fПригласить игрока в клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan accept &8- &fПринять приглашение в клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan decline &8- &fОтклонить приглашение в клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan kick {игрок} &8- &fКикнуть игрока"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan leave &8- &fПокинуть клан"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan pvp &8- &fИзменить режим PvP в клане"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan chat {сообщение} &8- &fНаписать в клановый чат"));
        player.sendMessage(MessageUtil.translate("&#A9BBF8/clan setLeader {игрок} &8- &fПередать роль лидера"));
        
        player.sendMessage("");
        player.sendMessage(MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        player.sendMessage("");
    }
}
