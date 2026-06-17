package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;

public class ClanCreateCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanCreateCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan create {имя}");
            return;
        }

        String clanName = args[0];
        int minLength = plugin.getConfig().getInt("clan.min-name-length", 3);
        int maxLength = plugin.getConfig().getInt("clan.max-name-length", 16);

        if (clanName.length() < minLength || clanName.length() > maxLength) {
            MessageUtil.sendError(player, "Название клана должно быть от " + minLength + " до " + maxLength + " символов");
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            if (repo.getClanByName(clanName).isPresent()) {
                MessageUtil.sendError(player, "Клан с таким названием уже существует");
                return;
            }

            Clan clan = new Clan();
            clan.setName(clanName);
            clan.setLeaderUUID(player.getUniqueId());
            clan.setColor(plugin.getConfig().getString("clan.default-color", "&7"));
            clan.setPvpEnabled(plugin.getConfig().getBoolean("pvp.enabled", true));

            repo.createClan(clan);

            ClanMember leader = new ClanMember();
            leader.setPlayerUUID(player.getUniqueId());
            leader.setPlayerName(player.getName());
            leader.setRole("LEADER");

            MessageUtil.sendSuccess(player, "Клан успешно создан!");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка при создании клана");
        }
    }
}
