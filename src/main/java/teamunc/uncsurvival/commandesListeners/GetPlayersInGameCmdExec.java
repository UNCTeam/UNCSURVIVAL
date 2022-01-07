package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.logic.GameManager;
import teamunc.uncsurvival.utils.MessageTchatManager;

public class GetPlayersInGameCmdExec implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MessageTchatManager.getInstance().sendMessageToPlayer("Players : " + GameManager.getInstance().getPlayersInGame(), sender, ChatColor.GREEN);
        return true;
    }
}
