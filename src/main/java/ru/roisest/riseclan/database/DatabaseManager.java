package ru.roisest.riseclan.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.Plugin;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private HikariDataSource dataSource;
    private Plugin plugin;

    public DatabaseManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean connect() {
        try {
            String host = plugin.getConfig().getString("mysql.host");
            int port = plugin.getConfig().getInt("mysql.port");
            String database = plugin.getConfig().getString("mysql.database");
            String username = plugin.getConfig().getString("mysql.username");
            String password = plugin.getConfig().getString("mysql.password");
            int poolSize = plugin.getConfig().getInt("mysql.pool-size");

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(poolSize);
            config.setMaxLifetime(plugin.getConfig().getLong("mysql.max-lifetime"));

            dataSource = new HikariDataSource(config);

            // Create tables
            createTables();

            plugin.getLogger().info("Connected to MySQL database!");
            return true;
        } catch (Exception e) {
            plugin.getLogger().severe("Could not connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void createTables() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Clans table
            stmt.execute("CREATE TABLE IF NOT EXISTS clans ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(16) UNIQUE NOT NULL,"
                    + "leader VARCHAR(36) NOT NULL,"
                    + "vice_leader VARCHAR(36),"
                    + "color VARCHAR(10) DEFAULT '&7',"
                    + "kills INT DEFAULT 0,"
                    + "deaths INT DEFAULT 0,"
                    + "level INT DEFAULT 1,"
                    + "pvp_enabled BOOLEAN DEFAULT true,"
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");

            // Members table
            stmt.execute("CREATE TABLE IF NOT EXISTS clan_members ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "clan_id INT NOT NULL,"
                    + "player_uuid VARCHAR(36) NOT NULL UNIQUE,"
                    + "player_name VARCHAR(16),"
                    + "role VARCHAR(20) DEFAULT 'MEMBER',"
                    + "joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE"
                    + ")");

            // Invitations table expected by repository (clan_invitations)
            stmt.execute("CREATE TABLE IF NOT EXISTS clan_invitations ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "clan_id INT NOT NULL,"
                    + "player_uuid VARCHAR(36) NOT NULL,"
                    + "player_name VARCHAR(64),"
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE"
                    + ")");

            // Backwards-compatible invites table (clan_invites)
            stmt.execute("CREATE TABLE IF NOT EXISTS clan_invites ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "clan_id INT NOT NULL,"
                    + "player_uuid VARCHAR(36) NOT NULL,"
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE"
                    + ")");

            plugin.getLogger().info("Database tables initialized!");
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
