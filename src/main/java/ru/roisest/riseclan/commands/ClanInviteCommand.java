package ru.roisest.riseclan.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.roisest.riseclan.RiseClans;
import ru.roisest.riseclan.database.ClanRepository;
import ru.roisest.riseclan.model.Clan;
import ru.roisest.riseclan.model.ClanMember;
import ru.roisest.riseclan.utils.MessageUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClanInviteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInviteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendFromConfig(player, "error-db", null);
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> playerClanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!playerClanOpt.isPresent()) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Clan clan = playerClanOpt.get();

            int maxMembers = plugin.getConfig().getInt("clan.max-members", 10);
            List<ClanMember> currentMembers = repo.getClanMembers(clan.getId());
            if (currentMembers.size() >= maxMembers) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageUtil.sendFromConfig(player, "player-not-found", Map.of("player", args[0]));
                return;
            }
            
            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                MessageUtil.sendFromConfig(player, "no-permission", null);
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (targetClan.isPresent()) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }

            if (repo.hasInvitation(targetPlayer.getUniqueId())) {
                MessageUtil.sendFromConfig(player, "error-db", null);
                return;
            }
            
            repo.createInvitation(clan.getId(), targetPlayer.getUniqueId(), targetPlayer.getName());
            
            MessageUtil.sendFromConfig(player, "invite-sent", Map.of("player", targetPlayer.getName()));
            
            // Отправляем красивое сообщение с кликабельной кнопкой
            sendInvitationMessage(targetPlayer, clan.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendFromConfig(player, "error-db", null);
        }
    }
    
    private void sendInvitationMessage(Player player, String clanName) {
        player.sendMessage("");
        
        // Верхняя линия с градиентом
        player.spigot().sendMessage(new TextComponent(MessageUtil.translate(MessageUtil.getConfigString("prefix") + "&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")));
        
        // Основное сообщение
        TextComponent message = new TextComponent(MessageUtil.translate(MessageUtil.getConfigString("invited") == null ? "&#A9BBF8▸ &fВы получили приглашение в клан &b" + clanName : MessageUtil.getConfigString("invited").replace("{clan}", clanName)));
        player.spigot().sendMessage(message);
        
        player.sendMessage("");
        
        // Кнопка принятия приглашения
        String acceptText = MessageUtil.getConfigString("invite-accept-button") != null ? MessageUtil.getConfigString("invite-accept-button") : "[✓ ПРИНЯТЬ]";
        String acceptHover = MessageUtil.getConfigString("invite-accept-hover") != null ? MessageUtil.getConfigString("invite-accept-hover") : "Нажмите, чтобы присоединиться к клану";
        TextComponent acceptButton = new TextComponent(MessageUtil.translate(acceptText));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder(MessageUtil.translate(acceptHover)).create()));
        
        // Разделитель
        TextComponent separator = new TextComponent("  ");
        
        // Кнопка отклонения приглашения
        String declineText = MessageUtil.getConfigString("invite-decline-button") != null ? MessageUtil.getConfigString("invite-decline-button") : "[✗ ОТКЛОНИТЬ]";
        String declineHover = MessageUtil.getConfigString("invite-decline-hover") != null ? MessageUtil.getConfigString("invite-decline-hover") : "Нажмите, чтобы отклонить приглашение";
        TextComponent declineButton = new TextComponent(MessageUtil.translate(declineText));
        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan decline"));
        declineButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder(MessageUtil.translate(declineHover)).create()));
        
        // Отправляем кнопки
        TextComponent line = new TextComponent();
        line.addExtra(acceptButton);
        line.addExtra(separator);
        line.addExtra(declineButton);
        player.spigot().sendMessage(line);
        
        player.sendMessage("");
        
        // Нижняя линия с градиентом
        player.spigot().sendMessage(new TextComponent(MessageUtil.translate(MessageUtil.getConfigString("prefix") + "&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")));
        
        player.sendMessage("");
    }
}
