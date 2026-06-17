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
                     "INSERT INTO clans (name, leader, color) VALUES (?, ?, ?)")) {
            stmt.setString(1, clan.getName());
            stmt.setString(2, clan.getLeaderUUID().toString());
            stmt.setString(3, clan.getColor());
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

    public void removeMember(int clanId, UUID playerUUID) throws SQLException {
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

    public Optional<ClanMember> getMember(int clanId, UUID playerUUID) throws SQLException {
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

    public void updateMemberRole(int clanId, UUID playerUUID, String role) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE clan_members SET role = ? WHERE clan_id = ? AND player_uuid = ?")) {
            stmt.setString(1, role);
            stmt.setInt(2, clanId);
            stmt.setString(3, playerUUID.toString());
            stmt.executeUpdate();
        }
    }

    private Clan mapClan(ResultSet rs) throws SQLException {
        Clan clan = new Clan();
        clan.setId(rs.getInt("id"));
        clan.setName(rs.getString("name"));
        clan.setLeaderUUID(UUID.fromString(rs.getString("leader")));
        String viceLeader = rs.getString("vice_leader");
        if (viceLeader != null) {
            clan.setViceLeaderUUID(UUID.fromString(viceLeader));
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
        member.setPlayerUUID(UUID.fromString(rs.getString("player_uuid")));
        member.setPlayerName(rs.getString("player_name"));
        member.setRole(rs.getString("role"));
        return member;
    }
}