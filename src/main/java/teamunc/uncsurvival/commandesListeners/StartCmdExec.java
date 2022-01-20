package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;

public class StartCmdExec extends AbstractCommandExecutor implements CommandExecutor {

    public StartCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // error if Game is already Running
        if (this.plugin.getGameManager().getGameStats().isGameStarted()) {
            this.messageTchatManager.sendMessageToPlayer("Game has already started !",sender, ChatColor.RED);
            return false;
        }

        // error if playersInGame < 1
        if (this.plugin.getGameManager().getParticipantManager().getGamePlayers().size() < 1) {
            this.messageTchatManager.sendMessageToPlayer("You need a minimum of 1 player in the game !", sender, ChatColor.RED);
            return false;
        }

        this.messageTchatManager.sendMessageToPlayer("The Game has been started.",sender, ChatColor.GREEN);

        // Lance le timer pour start
        this.plugin.getGameManager().initStarting();

        return true;
    }
}
