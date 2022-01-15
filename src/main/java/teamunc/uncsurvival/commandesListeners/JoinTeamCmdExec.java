package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;

public class JoinTeamCmdExec extends AbstractCommandExecutor{
    public JoinTeamCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean commandValid = true;
        ArrayList<String> playersName = new ArrayList<>();
        String teamColor = "";

        if(args.length >= 2) {
            teamColor = args[0];
            for (int i = 1; i < args.length; i++) {
                playersName.add(args[i]);
            }
            if ( ChatColor.valueOf(teamColor) == null ) {
                this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\"",sender, ChatColor.RED);
                return false;
            }
            Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColor));
            if(team == null) {
                this.messageTchatManager.sendMessageToPlayer("La team n'existe pas",sender, ChatColor.RED);
                return false;
            }
            for (String playerName : playersName) {
                try {
                    this.plugin.getGameManager().getParticipantManager().addPlayer(team, playerName);
                } catch (Exception e) {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer(e.toString(),sender, ChatColor.RED);
                }
            }
        } else {
            commandValid = false;
            this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\" and names of players to add",sender, ChatColor.RED);
        }

        if (commandValid) this.messageTchatManager.sendMessageToPlayer("Player(s) " + playersName + " have been successfully added from the team " + teamColor,sender, ChatColor.GREEN);

        return commandValid;
    }
}
