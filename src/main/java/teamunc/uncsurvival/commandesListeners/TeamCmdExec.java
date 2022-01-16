package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;

public class TeamCmdExec extends AbstractCommandExecutor{
    public TeamCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean commandValid = true;
        switch ( label ) {
            case "addteam":
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
            break;
            case "removeteam":
                if (args.length == 1) {

                    // Name
                    String name = args[0];

                    // Team remove
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
                break;
            case "addplayertoteam":
                ArrayList<String> playersNameAdded = new ArrayList<>();
                String teamColorAdded = "";

                if(args.length >= 2) {
                    teamColorAdded = args[0];
                    for (int i = 1; i < args.length; i++) {
                        playersNameAdded.add(args[i]);
                    }
                    if ( ChatColor.valueOf(teamColorAdded) == null ) {
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\"",sender, ChatColor.RED);
                        return false;
                    }
                    Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorAdded));
                    if(team == null) {
                        this.messageTchatManager.sendMessageToPlayer("La team n'existe pas",sender, ChatColor.RED);
                        return false;
                    }
                    for (String playerName : playersNameAdded) {
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

                if (commandValid) this.messageTchatManager.sendMessageToPlayer("Player(s) " + playersNameAdded + " have been successfully added from the team " + teamColorAdded,sender, ChatColor.GREEN);

                break;
            case "removeplayertoteam":
                ArrayList<String> playersNameRemoved = new ArrayList<>();
                String teamColorRemoved = "";

                if(args.length >= 2) {
                    teamColorRemoved = args[0];
                    for (int i = 1; i < args.length; i++) {
                        playersNameRemoved.add(args[i]);
                    }
                    if ( ChatColor.valueOf(teamColorRemoved) != null ) {
                        Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorRemoved));

                        for (String playerName : playersNameRemoved) {
                            this.plugin.getGameManager().getParticipantManager().removePlayer(playerName);
                        }
                    } else {
                        commandValid = false;
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\"",sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\" and names of players to add",sender, ChatColor.RED);
                }

                if (commandValid) this.messageTchatManager.sendMessageToPlayer("Player(s) " + playersNameRemoved + "has been successfully removed from the team " + teamColorRemoved,sender, ChatColor.GREEN);

                break;
        }
        return commandValid;
    }
}
