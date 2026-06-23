package ru.roisest.riseclan.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Arrays;

public class ClanCommand implements CommandExecutor {
    private RiseClans plugin;
    private ClanHelpCommand helpCommand;
    private ClanCreateCommand createCommand;
    private ClanDeleteCommand deleteCommand;
    private ClanInfoCommand infoCommand;
    private ClanInviteCommand inviteCommand;
    private ClanAcceptCommand acceptCommand;
    private ClanDeclineCommand declineCommand;
    private ClanKickCommand kickCommand;
    private ClanLeaveCommand leaveCommand;
    private ClanPvpCommand pvpCommand;
    private ClanChatCommand chatCommand;
    private ClanSetLeaderCommand setLeaderCommand;

    public ClanCommand(RiseClans plugin) {
        this.plugin = plugin;
        this.helpCommand = new ClanHelpCommand(plugin);
        this.createCommand = new ClanCreateCommand(plugin);
        this.deleteCommand = new ClanDeleteCommand(plugin);
        this.infoCommand = new ClanInfoCommand(plugin);
        this.inviteCommand = new ClanInviteCommand(plugin);
        this.acceptCommand = new ClanAcceptCommand(plugin);
        this.declineCommand = new ClanDeclineCommand(plugin);
        this.kickCommand = new ClanKickCommand(plugin);
        this.leaveCommand = new ClanLeaveCommand(plugin);
        this.pvpCommand = new ClanPvpCommand(plugin);
        this.chatCommand = new ClanChatCommand(plugin);
        this.setLeaderCommand = new ClanSetLeaderCommand(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            helpCommand.execute(player, new String[]{});
            return true;
        }

        String subCommand = args[0].toLowerCase();
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        switch (subCommand) {
            case "help":
                helpCommand.execute(player, newArgs);
                break;
            case "create":
                createCommand.execute(player, newArgs);
                break;
            case "delete":
                deleteCommand.execute(player, newArgs);
                break;
            case "info":
                infoCommand.execute(player, newArgs);
                break;
            case "invite":
                inviteCommand.execute(player, newArgs);
                break;
            case "accept":
                acceptCommand.execute(player, newArgs);
                break;
            case "decline":
                declineCommand.execute(player, newArgs);
                break;
            case "kick":
                kickCommand.execute(player, newArgs);
                break;
            case "leave":
                leaveCommand.execute(player, newArgs);
                break;
            case "pvp":
                pvpCommand.execute(player, newArgs);
                break;
            case "chat":
                chatCommand.execute(player, newArgs);
                break;
            case "setleader":
                setLeaderCommand.execute(player, newArgs);
                break;
            default:
                MessageUtil.sendError(player, "Неизвестная команда. Используйте /clan help");
                break;
        }

        return true;
    }
}
