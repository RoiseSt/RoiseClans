package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;
import ru.roisest.riseclan.database.ClanRepository;
import java.util.Optional;

public class ClanCreateCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanCreateCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            // usage not present in provided messages block -> fallback to error-db
            MessageUtil.sendFromConfig(player, "error-db", null);
            return;
        }

        String clanName = args[0];
        int minLength = plugin.getConfig().getInt("clan.min-name-length", 3);
        int maxLength = plugin.getConfig().getInt("clan.max-name-length", 16);

        // Проверка на наличие только цифр
        if (!clanName.matches("\\d+")) {
            // no specific key provided -> fallback
            MessageUtil.sendFromConfig(player, "error-db", null);
            return;
        }

        if (clanName.length() < minLength || clanName.length() > maxLength) {
            // no specific key provided -> fallback
            MessageUtil.sendFromConfig(player, "error-db", null);
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> playerClan = repo.getClanByLeader(player.getUniqueId());
            if (playerClan.isPresent()) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }
            
            Optional<Clan> memberClan = repo.getClanByMember(player.getUniqueId());
            if (memberClan.isPresent()) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }
            
            if (repo.getClanByName(clanName).isPresent()) {
                MessageUtil.sendFromConfig(player, "clan-exists", null);
                return;
            }

            Clan clan = new Clan();
            clan.setName(clanName);
            clan.setLeaderUUID(player.getUniqueId());
            clan.setColor(plugin.getConfig().getString("clan.default-color", "&7"));
            clan.setPvpEnabled(plugin.getConfig().getBoolean("pvp.enabled", true));

            repo.createClan(clan);
            
            Optional<Clan> createdClan = repo.getClanByName(clanName);
            if (createdClan.isPresent()) {
                ClanMember leader = new ClanMember();
                leader.setPlayerUUID(player.getUniqueId());
                leader.setPlayerName(player.getName());
                leader.setRole("LEADER");
                
                repo.addMember(createdClan.get().getId(), leader);
                MessageUtil.sendFromConfig(player, "clan-created", java.util.Map.of("clan", clanName));
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
}
