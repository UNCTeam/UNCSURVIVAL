package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;

public class MessageTchatManager extends AbstractManager{

    public MessageTchatManager(UNCSurvival plugin) {
        super(plugin);
    }

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
