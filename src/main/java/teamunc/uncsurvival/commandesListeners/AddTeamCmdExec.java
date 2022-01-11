package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;
import teamunc.uncsurvival.logic.team.Team;

public class AddTeamCmdExec extends AbstractCommandExecutor{
    public AddTeamCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean commandValid = true;

        if (args.length == 2 && sender instanceof Player ) {
            Player player = (Player) sender;

            // Name
            String name = args[0];

            // Color
            ChatColor color = ChatColor.valueOf(args[1]);

            // Location at player command executed
            Location location = player.getLocation();

            // Team creation
            Team t = UNCSurvival.getInstance().getGameManager().getTeamsManager().addTeam(name,color,location);

            if (t == null)
                plugin.getMessageTchatManager().sendMessageToPlayer("You can't add the team " + name + ".",
                        sender,ChatColor.RED);
            else
                plugin.getMessageTchatManager().sendMessageToPlayer("Your Team " + t.getChatColor() + t.getName() + ChatColor.GREEN + " has been successfully created !",
                        sender,ChatColor.GREEN);

        } else {
            commandValid = false;
        }
        return commandValid;
    }
}
