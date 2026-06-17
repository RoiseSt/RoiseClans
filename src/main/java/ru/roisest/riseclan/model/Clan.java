package ru.roisest.riseclan.model;

import java.util.UUID;

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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public UUID getLeaderUUID() {
        return leaderUUID;
    }

    public void setLeaderUUID(UUID leaderUUID) {
        this.leaderUUID = leaderUUID;
    }


    public UUID getViceLeaderUUID() {
        return viceLeaderUUID;
    }

    public void setViceLeaderUUID(UUID viceLeaderUUID) {
        this.viceLeaderUUID = viceLeaderUUID;
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }


    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }


    public double getKDRatio() {
        if (deaths == 0) {
            return kills;
        }

        return (double) kills / deaths;
    }
}