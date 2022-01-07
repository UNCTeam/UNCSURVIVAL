package teamunc.uncsurvival.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageTchatManager {

    //# SINGLETON
    private static MessageTchatManager instance;
    private MessageTchatManager() {}
    public static MessageTchatManager getInstance() {
        if (MessageTchatManager.instance == null) MessageTchatManager.instance = new MessageTchatManager();
        return MessageTchatManager.instance;
    }
    //# END SINGLETON

    private String prefix = "[UNC-SURVIVAL] : ";

    public void sendGeneralMesssage(String message, ChatColor color) {
        Bukkit.broadcastMessage(this.prefix + color + message);
    }
    public void sendGeneralMesssage(String message) {
        Bukkit.broadcastMessage(this.prefix + message);
    }

    public void sendMessageToPlayer(String message, CommandSender sender) {
        sender.sendMessage(this.prefix + message);
    }

    public void sendMessageToPlayer(String message, CommandSender sender, ChatColor color) {
        sender.sendMessage(this.prefix + color + message);
    }

}
