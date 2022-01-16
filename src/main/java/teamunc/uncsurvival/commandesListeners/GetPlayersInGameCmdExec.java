package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.Map;

public class GetPlayersInGameCmdExec extends AbstractCommandExecutor implements CommandExecutor {
    public GetPlayersInGameCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("getplayersingame")) {
            String playerList = "Players : ";
            for(Map.Entry<GamePlayer, Team> mapentry : this.plugin.getGameManager().getParticipantManager().getPlayersByTeam().entrySet()) {
                playerList += mapentry.getValue().getChatColor() + mapentry.getKey().getBukkitPlayer().getDisplayName() + ChatColor.WHITE + ", ";
            }
            sender.sendMessage(playerList);
        } else if(command.getName().equalsIgnoreCase("getplayersinteam")) {

        }
        return true;
    }
}
