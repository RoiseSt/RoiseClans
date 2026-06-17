package ru.roisest.riseclan.database;

import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import java.sql.*;
import java.util.*;

public class ClanRepository {
    private DatabaseManager databaseManager;

    public ClanRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void createClan(Clan clan) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO clans (name, leader, color, pvp_enabled) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, clan.getName());
            stmt.setString(2, clan.getLeaderUUID().toString());
            stmt.setString(3, clan.getColor());
            stmt.setBoolean(4, clan.isPvpEnabled());
            stmt.executeUpdate();
        }
    }

    public void deleteClan(int clanId) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM clans WHERE id = ?")) {
            stmt.setInt(1, clanId);
            stmt.executeUpdate();
        }
    }
    
    public void deleteClanMembers(int clanId) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM clan_members WHERE clan_id = ?")) {
            stmt.setInt(1, clanId);
            stmt.executeUpdate();
        }
    }

    public Optional<Clan> getClanByName(String name) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clans WHERE name = ?")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClan(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Clan> getClanById(int id) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clans WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClan(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    public Optional<Clan> getClanByLeader(java.util.UUID leaderUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clans WHERE leader = ?")) {
            stmt.setString(1, leaderUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClan(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    public Optional<Clan> getClanByMember(java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.* FROM clans c JOIN clan_members cm ON c.id = cm.clan_id WHERE cm.player_uuid = ?")) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClan(rs));
                }
            }
        }
        return Optional.empty();
    }
    
    public Optional<Clan> getClanByMemberRole(java.util.UUID playerUUID, String role) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.* FROM clans c JOIN clan_members cm ON c.id = cm.clan_id WHERE cm.player_uuid = ? AND cm.role = ?")) {
            stmt.setString(1, playerUUID.toString());
            stmt.setString(2, role);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClan(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Clan> getTopClans(String sortBy, int limit) throws SQLException {
        List<Clan> clans = new ArrayList<>();
        String query = "SELECT * FROM clans ORDER BY " + 
                ("kills".equals(sortBy) ? "kills DESC" : "level DESC") + 
                " LIMIT ?";
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clans.add(mapClan(rs));
                }
            }
        }
        return clans;
    }

    public void updateClan(Clan clan) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE clans SET leader=?, vice_leader=?, color=?, kills=?, deaths=?, level=?, pvp_enabled=? WHERE id=?")) {
            stmt.setString(1, clan.getLeaderUUID().toString());
            stmt.setString(2, clan.getViceLeaderUUID() != null ? clan.getViceLeaderUUID().toString() : null);
            stmt.setString(3, clan.getColor());
            stmt.setInt(4, clan.getKills());
            stmt.setInt(5, clan.getDeaths());
            stmt.setInt(6, clan.getLevel());
            stmt.setBoolean(7, clan.isPvpEnabled());
            stmt.setInt(8, clan.getId());
            stmt.executeUpdate();
        }
    }

    public void addMember(int clanId, ClanMember member) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO clan_members (clan_id, player_uuid, player_name, role) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, clanId);
            stmt.setString(2, member.getPlayerUUID().toString());
            stmt.setString(3, member.getPlayerName());
            stmt.setString(4, member.getRole());
            stmt.executeUpdate();
        }
    }

    public void removeMember(int clanId, java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM clan_members WHERE clan_id = ? AND player_uuid = ?")) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            stmt.executeUpdate();
        }
    }

    public List<ClanMember> getClanMembers(int clanId) throws SQLException {
        List<ClanMember> members = new ArrayList<>();
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM clan_members WHERE clan_id = ? ORDER BY role DESC")) {
            stmt.setInt(1, clanId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    members.add(mapClanMember(rs));
                }
            }
        }
        return members;
    }

    public Optional<ClanMember> getMember(int clanId, java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM clan_members WHERE clan_id = ? AND player_uuid = ?")) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapClanMember(rs));
                }
            }
        }
        return Optional.empty();
    }

    public void updateMemberRole(int clanId, java.util.UUID playerUUID, String role) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE clan_members SET role = ? WHERE clan_id = ? AND player_uuid = ?")) {
            stmt.setString(1, role);
            stmt.setInt(2, clanId);
            stmt.setString(3, playerUUID.toString());
            stmt.executeUpdate();
        }
    }
    
    public boolean hasInvitation(java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT 1 FROM clan_invitations WHERE player_uuid = ? LIMIT 1")) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public void createInvitation(int clanId, java.util.UUID playerUUID, String playerName) throws SQLException {
        // Удаляем старые приглашения перед созданием нового
        deleteInvitation(playerUUID);
        
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO clan_invitations (clan_id, player_uuid, player_name) VALUES (?, ?, ?)")) {
            stmt.setInt(1, clanId);
            stmt.setString(2, playerUUID.toString());
            stmt.setString(3, playerName);
            stmt.executeUpdate();
        }
    }
    
    public Optional<Integer> getLatestInvitation(java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT clan_id FROM clan_invitations WHERE player_uuid = ? ORDER BY id DESC LIMIT 1")) {
            stmt.setString(1, playerUUID.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("clan_id"));
                }
            }
        }
        return Optional.empty();
    }
    
    public void deleteInvitation(java.util.UUID playerUUID) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "DELETE FROM clan_invitations WHERE player_uuid = ?")) {
            stmt.setString(1, playerUUID.toString());
            stmt.executeUpdate();
        }
    }

    private Clan mapClan(ResultSet rs) throws SQLException {
        Clan clan = new Clan();
        clan.setId(rs.getInt("id"));
        clan.setName(rs.getString("name"));
        clan.setLeaderUUID(java.util.UUID.fromString(rs.getString("leader")));
        String viceLeader = rs.getString("vice_leader");
        if (viceLeader != null) {
            clan.setViceLeaderUUID(java.util.UUID.fromString(viceLeader));
        }
        clan.setColor(rs.getString("color"));
        clan.setKills(rs.getInt("kills"));
        clan.setDeaths(rs.getInt("deaths"));
        clan.setLevel(rs.getInt("level"));
        clan.setPvpEnabled(rs.getBoolean("pvp_enabled"));
        return clan;
    }

    private ClanMember mapClanMember(ResultSet rs) throws SQLException {
        ClanMember member = new ClanMember();
        member.setPlayerUUID(java.util.UUID.fromString(rs.getString("player_uuid")));
        member.setPlayerName(rs.getString("player_name"));
        member.setRole(rs.getString("role"));
        return member;
    }
}
