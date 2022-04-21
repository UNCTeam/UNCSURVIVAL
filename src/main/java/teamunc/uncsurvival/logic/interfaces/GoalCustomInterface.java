package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.Team;

public class GoalCustomInterface extends GameCustomInterface{

    private Team team;
    /**
     *
     * @param itemNumber le nombre item goal (a voir sur le trello)
     */
    public GoalCustomInterface(int itemNumber, Team t) {
        super(itemNumber);
        this.team = t;
    }

    @Override
    public Inventory update() {

        // TODO remove after phase 2
        if (this.itemNumber == 4 && team.getItemsProduction(4) >= 5000) {
            UNCSurvival.getInstance().getGameManager().getParticipantManager().getGamePlayers().forEach(gamePlayer -> {
                if (gamePlayer.getBukkitPlayer() != null && gamePlayer.getTeamColor() == team.getChatColor()) {
                    UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer(" Vous avez atteint les 5 000 items de ce type ! Bravo !",gamePlayer.getBukkitPlayer(), ChatColor.GOLD);
                }
            });
        }


        int itemPrice = UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemPrice(this.itemNumber);
        int itemNumbers = this.team.getItemsProduction(this.itemNumber);
        this.updateName(this.itemNumber);
        String itemPriceStr = this.reduceAt(21) + "       " + translateInInterfaceDisplay(""+ itemPrice,1);
        String itemNumberStr = this.reduceAt(4) + translateInInterfaceDisplay(""+ itemNumbers,4);

        this.inv = Bukkit.createInventory(null,27,name + itemPriceStr + itemNumberStr);
        return this.inv;
    }
}
