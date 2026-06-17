package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

public class ClanInfoCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInfoCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Usage: /clan info {clan}");
            return;
        }

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            Optional<Clan> optional = repo.getClanByName(args[0]);

            if (!optional.isPresent()) {
                MessageUtil.sendError(player, "Clan not found");
                return;
            }

            Clan clan = optional.get();
            MessageUtil.sendInfo(player, "&8=== &a" + clan.getName() + " Info &8===");
            MessageUtil.sendInfo(player, "&aLevel: &7" + clan.getLevel());
            MessageUtil.sendInfo(player, "&aKills: &7" + clan.getKills());
            MessageUtil.sendInfo(player, "&aDeaths: &7" + clan.getDeaths());
            MessageUtil.sendInfo(player, "&aK/D Ratio: &7" + String.format("%.2f", clan.getKDRatio()));
            MessageUtil.sendInfo(player, "&aPvP: &7" + (clan.isPvpEnabled() ? "Enabled" : "Disabled"));
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "An error occurred");
        }
    }
}