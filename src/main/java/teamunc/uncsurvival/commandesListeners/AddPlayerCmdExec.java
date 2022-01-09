package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;

public class AddPlayerCmdExec extends AbstractCommandExecutor implements CommandExecutor {

    public AddPlayerCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // error if not enougth args
        if (args.length < 1) {
            this.messageTchatManager.sendMessageToPlayer("You need to specify a player !",sender, ChatColor.RED);
            return false;
        }

        for (String arg : args) {
            Player response = Bukkit.getPlayer(arg);
            if (response == null) {
                this.messageTchatManager.sendMessageToPlayer("The player " + arg + "Can't be add.",sender, ChatColor.GOLD);
            } else {
                boolean success = UNCSurvival.getInstance().getGameManager().addPlayerToTheGame(response);
                if (success) {
                    this.messageTchatManager.sendMessageToPlayer("The player " + arg + " has been added to the game.",sender, ChatColor.GREEN);
                }
            }
        }
        return true;
    }
}
