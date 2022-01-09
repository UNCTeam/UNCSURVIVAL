package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.utils.MessageTchatManager;

public class StartCmdExec extends abstractCommandExecutor implements CommandExecutor {

    public StartCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean res = this.plugin.getGameManager().start(sender);

        if (res) {
            MessageTchatManager.getInstance().sendMessageToPlayer("The Game has been started.",sender, ChatColor.GREEN);
        }

        return res;
    }
}
