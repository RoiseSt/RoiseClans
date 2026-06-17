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
            MessageUtil.sendError(player, "Usage: /clan create {name}");
            return;
        }

        String clanName = args[0];
        int minLength = plugin.getConfig().getInt("clan.min-name-length", 3);
        int maxLength = plugin.getConfig().getInt("clan.max-name-length", 16);

        if (clanName.length() < minLength || clanName.length() > maxLength) {
            MessageUtil.sendError(player, "Clan name must be between " + minLength + " and " + maxLength + " characters");
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            if (repo.getClanByName(clanName).isPresent()) {
                MessageUtil.sendError(player, "Clan with that name already exists");
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

            MessageUtil.sendSuccess(player, "Clan created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "An error occurred while creating the clan");
        }
    }
}