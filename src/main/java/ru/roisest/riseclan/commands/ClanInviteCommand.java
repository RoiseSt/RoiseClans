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
import ru.roisest.riseclan.utils.MessageUtil;
import java.util.Optional;

public class ClanInviteCommand implements IClanCommand {
    private RiseClans plugin;

    public ClanInviteCommand(RiseClans plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            MessageUtil.sendError(player, "Использование: /clan invite {игрок}");
            return;
        }
        
        try {
            ClanRepository repo = new ClanRepository(plugin.getDatabaseManager());
            
            Optional<Clan> playerClanOpt = repo.getClanByLeader(player.getUniqueId());
            if (!playerClanOpt.isPresent()) {
                MessageUtil.sendError(player, "Вы не являетесь лидером клана");
                return;
            }
            
            Clan clan = playerClanOpt.get();
            
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                MessageUtil.sendError(player, "Игрок не найден");
                return;
            }
            
            if (targetPlayer.getUniqueId().equals(player.getUniqueId())) {
                MessageUtil.sendError(player, "Вы не можете пригласить себя");
                return;
            }
            
            Optional<Clan> targetClan = repo.getClanByMember(targetPlayer.getUniqueId());
            if (targetClan.isPresent()) {
                MessageUtil.sendError(player, "Этот игрок уже состоит в клане");
                return;
            }
            
            repo.createInvitation(clan.getId(), targetPlayer.getUniqueId(), targetPlayer.getName());
            
            MessageUtil.sendSuccess(player, "Приглашение отправлено игроку " + targetPlayer.getName());
            
            // Отправляем красивое сообщение с кликабельной кнопкой
            sendInvitationMessage(targetPlayer, clan.getName());
            
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.sendError(player, "Произошла ошибка");
        }
    }
    
    private void sendInvitationMessage(Player player, String clanName) {
        player.sendMessage("");
        
        // Верхняя линия
        player.spigot().sendMessage(new TextComponent(MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")));
        
        // Основное сообщение
        TextComponent message = new TextComponent(MessageUtil.translate("&#A9BBF8▸ &fВы получили приглашение в клан &b" + clanName));
        player.spigot().sendMessage(message);
        
        player.sendMessage("");
        
        // Кнопка принятия приглашения
        TextComponent acceptButton = new TextComponent(MessageUtil.translate("&#A9BBF8[✓ ПРИНЯТЬ]"));
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan accept"));
        acceptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder(MessageUtil.translate("&#A9BBF8Нажмите, чтобы присоединиться к клану")).create()));
        
        // Разделитель
        TextComponent separator = new TextComponent("  ");
        
        // Кнопка отклонения приглашения
        TextComponent declineButton = new TextComponent(MessageUtil.translate("&#A9BBF8[✗ ОТКЛОНИТЬ]"));
        declineButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/clan decline"));
        declineButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
            new ComponentBuilder(MessageUtil.translate("&#A9BBF8Нажмите, чтобы отклонить приглашение")).create()));
        
        // Отправляем кнопки
        TextComponent line = new TextComponent();
        line.addExtra(acceptButton);
        line.addExtra(separator);
        line.addExtra(declineButton);
        player.spigot().sendMessage(line);
        
        player.sendMessage("");
        
        // Нижняя линия
        player.spigot().sendMessage(new TextComponent(MessageUtil.translate("&#A9BBF8⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯")));
        
        player.sendMessage("");
    }
}
