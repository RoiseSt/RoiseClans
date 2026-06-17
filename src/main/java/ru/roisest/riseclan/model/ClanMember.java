package ru.roisest.riseclan.model;

import lombok.Data;
import java.util.UUID;

@Data
public class ClanMember {
    private UUID playerUUID;
    private String playerName;
    private String role; // LEADER, VICE_LEADER, MEMBER

    public boolean isLeader() {
        return "LEADER".equals(role);
    }

    public boolean isViceLeader() {
        return "VICE_LEADER".equals(role);
    }
}