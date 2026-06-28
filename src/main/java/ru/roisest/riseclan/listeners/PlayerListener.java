package ru.roisest.riseclan.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.utils.MessageUtil;

import java.util.Optional;

public class PlayerListener implements Listener {
    private RiseClans plugin;

    public PlayerListener(RiseClans plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            // TODO: Add kill/death stats to database
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();
        Player attacker = null;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile proj = (Projectile) event.getDamager();
            if (proj.getShooter() instanceof Player) {
                attacker = (Player) proj.getShooter();
            }
        }

        if (attacker == null) return;

        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            Optional<Clan> victimClan = repo.getClanByMember(victim.getUniqueId());
            Optional<Clan> attackerClan = repo.getClanByMember(attacker.getUniqueId());

            if (victimClan.isPresent() && attackerClan.isPresent()) {
                Clan vClan = victimClan.get();
                Clan aClan = attackerClan.get();
                if (vClan.getId() == aClan.getId()) {
                    // Same clan: check clan's PvP setting
                    if (!vClan.isPvpEnabled()) {
                        // PvP disabled among clan members
                        event.setCancelled(true);
                        MessageUtil.sendFromConfig(attacker, "pvp-disabled", null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
