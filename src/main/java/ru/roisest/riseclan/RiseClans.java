package ru.roisest.riseclan;

import org.bukkit.plugin.java.JavaPlugin;
import ru.roisest.riseclan.database.DatabaseManager;
import ru.roisest.riseclan.commands.ClanCommand;
import ru.roisest.riseclan.listeners.PlayerListener;
import ru.roisest.riseclan.listeners.ClanChatListener;
import ru.roisest.riseclan.placeholders.ClanPlaceholder;
import org.bukkit.Bukkit;

public class RiseClans extends JavaPlugin {

    private static RiseClans instance;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initialize Database
        databaseManager = new DatabaseManager(this);
        if (!databaseManager.connect()) {
            getLogger().severe("Failed to connect to database!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        // Register Commands
        getCommand("clan").setExecutor(new ClanCommand(this));

        // Register Listeners
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClanChatListener(this), this);

        // Register PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ClanPlaceholder(this).register();
            getLogger().info("PlaceholderAPI hooked!");
        }

        getLogger().info("RiseClans v" + getDescription().getVersion() + " enabled!");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.disconnect();
        }
        getLogger().info("RiseClans disabled!");
    }

    public static RiseClans getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}