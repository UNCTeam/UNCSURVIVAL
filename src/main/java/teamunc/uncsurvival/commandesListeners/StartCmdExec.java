package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.logic.GameManager;
import teamunc.uncsurvival.utils.MessageTchatManager;

public class StartCmdExec implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean res = GameManager.getInstance().start(sender);

        if (res) {
            MessageTchatManager.getInstance().sendMessageToPlayer("The Game has been started.",sender, ChatColor.GREEN);
        }

        return res;
    }
}
