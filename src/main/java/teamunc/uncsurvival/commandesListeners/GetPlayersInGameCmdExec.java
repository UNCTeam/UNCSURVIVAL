package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;

public class GetPlayersInGameCmdExec extends AbstractCommandExecutor implements CommandExecutor {
    public GetPlayersInGameCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.messageTchatManager.sendMessageToPlayer("Players : " + this.plugin.getGameManager().getPlayersInGame(), sender, ChatColor.GREEN);
        return true;
    }
}
