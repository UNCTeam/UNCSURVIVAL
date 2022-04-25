package teamunc.uncsurvival.commandesListeners;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class PlayerCmdExec extends AbstractCommandExecutor{
    public PlayerCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gameManager = this.plugin.getGameManager();
        boolean commandValid = true;

        if (sender instanceof Player )
            switch ( label ) {
                case "food":
                case "f":
                    GamePlayer gamePlayer = gameManager.getParticipantManager().getGamePlayer(((Player) sender).getDisplayName());
                    String sendedMsg = "§8-| §b§lHistorique Nourriture §8|---------------\n";
                    for (Material material : gamePlayer.getQueueOfEatenFood()) {
                        if (gamePlayer.EatHowOften(material) >= 4 && (
                                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 ||
                                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE3
                            )
                        )
                            sendedMsg += "§c" + material.name().toLowerCase(Locale.ROOT).replace('_',' ');
                        else
                            sendedMsg += "§a" + material.name().toLowerCase(Locale.ROOT).replace('_',' ');


                        sendedMsg += "§8§l | ";
                    }
                    sender.sendMessage(sendedMsg);
                    break;

                case "modifyitemvalue":
                    // ITEM VALUE MODIFICATION
                    if (args.length == 2) {
                        try {
                            int num1 = Integer.parseInt(args[0]);
                            int num2 = Integer.parseInt(args[1]);

                            this.plugin.getGameManager().getItemsManager().setGoalItemPrice(num1,num2);

                            this.plugin.getMessageTchatManager().sendMessageToPlayer(
                                    "Nouvelle valeur de l'item "
                                    + this.plugin.getGameManager().getItemsManager().getGoalItemName(num1)
                                    + ChatColor.GOLD
                                    + " est maintenant "
                                    + ChatColor.LIGHT_PURPLE
                                    + this.plugin.getGameManager().getItemsManager().getGoalItemPrice(num1),sender, ChatColor.GOLD);
                        } catch (Exception ignored) {}
                    }
                    break;
                case "modifyitemnumber":
                    // ITEM VALUE MODIFICATION
                    if (args.length == 3) {
                        try {
                            String team = args[0];
                            int numIndex = Integer.parseInt(args[1]);
                            int newNumberItem = Integer.parseInt(args[2]);

                            Team t = this.plugin.getGameManager().getTeamsManager().getTeam(team);
                            Field field = Team.class.getDeclaredField("itemsProduction");
                            field.setAccessible(true);
                            ArrayList<Integer> list = (ArrayList<Integer>) field.get(t);
                            list.set(numIndex,newNumberItem);
                            this.plugin.getMessageTchatManager().sendMessageToPlayer("the "+ t.getName() + "'s items goals has been changed succesfully.",sender);
                        } catch (Exception ignored) {}
                    } else {
                        this.plugin.getMessageTchatManager().sendMessageToPlayer("modifyitemnumber team indexItem newNumberItem",sender);
                    }
                    break;
                case "openinv":
                    if(args.length == 1) {
                        Player targerPlayer = Bukkit.getPlayer(args[0]);
                        if(targerPlayer != null) {
                            ((Player) sender).openInventory(targerPlayer.getInventory());
                        }
                    }
                    break;
                case "openenderchest":
                    if(args.length == 1) {
                        Player targerPlayer = Bukkit.getPlayer(args[0]);
                        if(targerPlayer != null) {
                            ((Player) sender).openInventory(targerPlayer.getEnderChest());
                        }
                    }
                    break;
            }
        return commandValid;
    }
}
