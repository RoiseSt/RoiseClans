package ru.roisest.riseclan.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.roisest.riseclan.RiseClans;

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
}