package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;

public class RemoveTeamCmdExec extends AbstractCommandExecutor{
    public RemoveTeamCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean commandValid = true;

        if (args.length == 1) {

            // Name
            String name = args[0];

            // Team creation
            Team t = UNCSurvival.getInstance().getGameManager().getTeamsManager().removeTeam(name);
            if (t == null)
                plugin.getMessageTchatManager().sendMessageToPlayer("You can't delete the team " + name + ".",
                        sender,ChatColor.RED);
            else
                plugin.getMessageTchatManager().sendMessageToPlayer("Your team " + t.getChatColor() + t.getName() + ChatColor.GREEN + " has been successfully removed !",
                        sender,ChatColor.GREEN);
        } else {
            commandValid = false;
        }
        return commandValid;
    }
}
