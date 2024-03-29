package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.Advancement;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class TeamCmdExec extends AbstractCommandExecutor{
    public TeamCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gameManager = this.plugin.getGameManager();
        boolean commandValid = true;
        switch ( label ) {
            case "addteam": {
                if ( args.length == 2 && sender instanceof Player ) {
                    Player player = (Player) sender;

                    // Name
                    String name = args[0];

                    // Color
                    ChatColor color = ChatColor.valueOf(args[1]);

                    // Location at player command executed
                    Location location = player.getLocation();

                    // Team creation
                    Team t = UNCSurvival.getInstance().getGameManager().getTeamsManager().addTeam(name, color, location);

                    if ( t == null )
                        plugin.getMessageTchatManager().sendMessageToPlayer("You can't add the team " + name + ".",
                                sender, ChatColor.RED);
                    else
                        plugin.getMessageTchatManager().sendMessageToPlayer("Your Team " + t.getChatColor() + t.getName() + ChatColor.GREEN + " has been successfully created !",
                                sender, ChatColor.GREEN);

                } else {
                    commandValid = false;
                }
                break;
            }
            case "removeteam": {
                if ( args.length == 1 ) {

                    // Name
                    String name = args[0];

                    // Team remove
                    Team t = UNCSurvival.getInstance().getGameManager().getTeamsManager().removeTeam(name);
                    if ( t == null )
                        plugin.getMessageTchatManager().sendMessageToPlayer("You can't delete the team " + name + ".",
                                sender, ChatColor.RED);
                    else
                        plugin.getMessageTchatManager().sendMessageToPlayer("Your team " + t.getChatColor() + t.getName() + ChatColor.GREEN + " has been successfully removed !",
                                sender, ChatColor.GREEN);
                } else {
                    commandValid = false;
                }
                break;
            }
            case "addplayertoteam": {
                ArrayList<String> playersNameAdded = new ArrayList<>();
                String teamColorAdded = "";

                if ( args.length >= 2 ) {
                    teamColorAdded = args[0];
                    for (int i = 1; i < args.length; i++) {
                        playersNameAdded.add(args[i]);
                    }
                    if ( ChatColor.valueOf(teamColorAdded) == null ) {
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\"", sender, ChatColor.RED);
                        return false;
                    }
                    Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorAdded));
                    if ( team == null ) {
                        this.messageTchatManager.sendMessageToPlayer("La team n'existe pas", sender, ChatColor.RED);
                        return false;
                    }
                    for (String playerName : playersNameAdded) {
                        try {
                            this.plugin.getGameManager().getParticipantManager().addPlayer(team, playerName);

                        } catch (Exception e) {
                            commandValid = false;
                            this.messageTchatManager.sendMessageToPlayer(e.toString(), sender, ChatColor.RED);
                        }
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\" and names of players to add", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Player(s) " + playersNameAdded + " have been successfully added from the team " + teamColorAdded, sender, ChatColor.GREEN);

                break;
            }
            case "removeplayertoteam": {
                ArrayList<String> playersNameRemoved = new ArrayList<>();
                String teamColorRemoved = "";

                if ( args.length >= 2 ) {
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
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\"", sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like \"AQUA\" and names of players to add", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Player(s) " + playersNameRemoved + "has been successfully removed from the team " + teamColorRemoved, sender, ChatColor.GREEN);

                break;
            }
            case "addbonusscore": {
                final String teamColorBonusAdded = args[0];
                final int addedBonusScore = Integer.parseInt(args[1]);

                if ( args.length == 2 ) {

                    if ( gameManager.getTeamsManager().getTeam(ChatColor.valueOf(teamColorBonusAdded)) != null ) {
                        Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorBonusAdded));
                        team.addABonusScore(addedBonusScore);

                    } else {
                        commandValid = false;
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA", sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA and a number to add", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Added " + addedBonusScore + " as a bonus to the team " + teamColorBonusAdded, sender, ChatColor.GREEN);

                break;
        }
            case "removebonusscore": {
                final String teamColorBonusRemove = args[0];
                final int removedBonusScore = Integer.parseInt(args[1]);

                if ( args.length == 2 ) {

                    if ( gameManager.getTeamsManager().getTeam(ChatColor.valueOf(teamColorBonusRemove)) != null ) {
                        Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorBonusRemove));

                        team.addABonusScore(-removedBonusScore);

                    } else {
                        commandValid = false;
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA", sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA and a number to add", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Removed " + removedBonusScore + " as a bonus to the team " + teamColorBonusRemove, sender, ChatColor.GREEN);

                break;
            }
            case "addscore": {
                final String teamColorScoreAdded = args[0];
                final int addedScore = Integer.parseInt(args[1]);

                if ( args.length == 2 ) {

                    if ( gameManager.getTeamsManager().getTeam(ChatColor.valueOf(teamColorScoreAdded)) != null ) {
                        Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorScoreAdded));

                        team.addScore(addedScore);

                    } else {
                        commandValid = false;
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA", sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA and a number to add", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Added " + addedScore + " from the score of the team " + teamColorScoreAdded, sender, ChatColor.GREEN);

                break;
            }
            case "removescore": {
                final String teamColorScoreRemoved = args[0];
                final int removedScore = Integer.parseInt(args[1]);

                if ( args.length == 2 ) {

                    if ( gameManager.getTeamsManager().getTeam(ChatColor.valueOf(teamColorScoreRemoved)) != null ) {
                        Team team = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(teamColorScoreRemoved));

                        team.addScore(-removedScore);

                    } else {
                        commandValid = false;
                        this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA", sender, ChatColor.RED);
                    }
                } else {
                    commandValid = false;
                    this.messageTchatManager.sendMessageToPlayer("The command is incorrect ! Use Color like AQUA and a number to remove", sender, ChatColor.RED);
                }

                if ( commandValid )
                    this.messageTchatManager.sendMessageToPlayer("Removed " + removedScore + " from the score of the team " + teamColorScoreRemoved, sender, ChatColor.GREEN);
                break;
            }
            case "stats": {
                if ( sender instanceof Player ) {
                    Player player = (Player) sender;
                    if ( this.plugin.getGameManager().getParticipantManager().isPlaying(player) ) {
                        Team playerTeam = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(player);
                        player.sendMessage(playerTeam.getStats(player));
                    }
                }
                break;
            }
            case "statsteam": {
                Team teamStats = this.plugin.getGameManager().getTeamsManager().getTeam(ChatColor.valueOf(args[0]));
                sender.sendMessage(teamStats.getStats(null));
                break;
            }
            case "classement": {

                StringBuilder classementStr = new StringBuilder();
                classementStr.append("§8--------------| §b§lClassement §8|---------------\n \n");
                int count = 1;
                for (Team team : plugin.getGameManager().getTeamsManager().getClassement()) {
                    try {
                        Field field = Team.class.getDeclaredField("bonusScore");
                        field.setAccessible(true);
                        int bonusScore = (int) field.get(team);


                        classementStr.append(" " + team.getChatColor()).append(count);
                        classementStr.append(" - ").append(team.getName());
                        /*if(player.getDisplayName().equals("ValkyrieHD") || player.getDisplayName().equals("UNCDelsus")) {*/
                        classementStr.append(" : ").append(team.getScore()).append(" (" + (team.getScore() - bonusScore) + ")");
                        /*}*/
                        classementStr.append("\n");
                        count++;
                    } catch (Exception ignored) {}
                }
                sender.sendMessage(classementStr.toString());

                break;
            }
            case "phaseinfo": {
                if ( sender instanceof Player ) {
                    Player player = (Player) sender;
                    String phaseMsg = "§8--------------| §b§lPhase Info §8|---------------\n百\n\n百\n\n";
                    phaseMsg += "§f保\n" + "§8百\n百\n百\n百\n百\n百\n百\n百\n百\n百\n§f包\n百\n百\n百\n百\n百\n百";
                    player.sendMessage(phaseMsg);
                }
                break;
            }
            case "achievements": {
                    StringBuilder advancementStr = new StringBuilder();
                    advancementStr.append("§8--------------| §b§l Achievements §8|---------------\n \n");

                    for(Advancement adv : plugin.getGameManager().getAdvancementManager().getList()) {
                        advancementStr.append("§6- " + adv.DisplayedName() + " §b§l+" + adv.givenPoints() + " §r§6: ");
                        if (adv.alreadyGranted())
                            advancementStr.append(
                                this.plugin.getGameManager().getTeamsManager().getTeam(adv.getTeamColor()).getChatColor() +
                                this.plugin.getGameManager().getTeamsManager().getTeam(adv.getTeamColor()).getName()
                            );
                        else
                            advancementStr.append("§fEncore Possible !");

                        advancementStr.append("\n");
                    }
                    sender.sendMessage(advancementStr.toString());
                break;
            }
            case "startduel": {
                if ( args.length == 2 ) {
                    ArrayList<GamePlayer> playersSelected = new ArrayList<>();
                    playersSelected.add(this.plugin.getGameManager().getParticipantManager().getGamePlayer(args[0]));
                    playersSelected.add(this.plugin.getGameManager().getParticipantManager().getGamePlayer(args[1]));

                    gameManager.getGameEventsManager().startDuel(playersSelected);
                } else if (args.length == 0) {
                    // choix des joueurs
                    ArrayList<GamePlayer> playersSelected = this.plugin.getGameManager().getParticipantManager().getRandomOnlineGamePlayerFromDiffTeams(2);
                    gameManager.getGameEventsManager().startDuel(playersSelected);
                } else {
                    this.plugin.getMessageTchatManager().sendMessageToPlayer("Il faut 2 joueurs !", sender, ChatColor.RED);
                }
                break;
            }
        }
        return commandValid;
    }
}
