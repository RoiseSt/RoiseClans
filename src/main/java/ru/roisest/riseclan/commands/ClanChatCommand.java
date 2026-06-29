package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class ClanChatCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanChatCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendFromConfig(player, "usage-chat", null);
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> clanOpt = repo.getClanByMember(player.getUniqueId());
            if (!clanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Clan clan = clanOpt.get();
            
            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }
            
            String chatMessage = message.toString().trim();
            String configFormat = MessageUtil.getConfigString("chat-format");
            
            if (configFormat != null) {
                String finalMessage = configFormat
                    .replace("{clan}", clan.getName())
                    .replace("{player}", player.getName())
                    .replace("{message}", chatMessage);
                
                List<ClanMember> members = repo.getClanMembers(clan.getId());
                for (ClanMember member : members) {
                    Player onlinePlayer = org.bukkit.Bukkit.getPlayer(member.getPlayerUUID());
                    if (onlinePlayer != null && onlinePlayer.isOnline()) {
                        MessageUtil.sendChat(onlinePlayer, finalMessage);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
