package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.List;

public class ClanHelpCommand implements IClanCommand {

    private final RiseClans plugin;

    public ClanHelpCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("");
        player.sendMessage(MessageUtil.translate(
            MessageUtil.getConfigString("prefix") + 
            MessageUtil.getConfigString("help-header")
        ));
        player.sendMessage("");
        
        List<String> helpLines = MessageUtil.getConfigStringList("help-lines");
        for (String line : helpLines) {
            player.sendMessage(MessageUtil.translate(line));
        }
        
        player.sendMessage("");
        player.sendMessage(MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        player.sendMessage("");
    }
}
