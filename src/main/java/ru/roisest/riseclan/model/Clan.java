package ru.roisest.riseclan.model;

import lombok.Data;
import java.util.*;

@Data
public class Clan {
    private int id;
    private String name;
    private UUID leaderUUID;
    private UUID viceLeaderUUID;
    private String color;
    private int kills;
    private int deaths;
    private int level;
    private boolean pvpEnabled;
    private List<ClanMember> members = new ArrayList<>();

    public double getKDRatio() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }
}