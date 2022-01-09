package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.utils.MessageTchatManager;

public class RemovePlayerCmdExec extends abstractCommandExecutor implements CommandExecutor {
    public RemovePlayerCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // error if not enougth args
        if (args.length < 1) {
            MessageTchatManager.getInstance().sendMessageToPlayer("You need to specify a player !",sender, ChatColor.RED);
            return false;
        }

        for (String arg : args) {
            Player response = Bukkit.getPlayer(arg);
            if (response == null) {
                MessageTchatManager.getInstance().sendMessageToPlayer("The player " + arg + "Can't be removed.",sender, ChatColor.GOLD);
            } else {
                boolean success = this.plugin.getGameManager().removePlayerToTheGame(response);
                if (success) {
                    MessageTchatManager.getInstance().sendMessageToPlayer("The player " + arg + " has been removed to the game.",sender, ChatColor.GREEN);
                }
            }
        }
        return true;
    }
}
