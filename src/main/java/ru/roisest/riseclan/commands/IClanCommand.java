package ru.roisest.riseclan.commands;

import org.bukkit.entity.Player;

public interface IClanCommand {
    void execute(Player player, String[] args);
}