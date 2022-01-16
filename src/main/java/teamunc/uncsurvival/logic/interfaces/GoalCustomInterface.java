package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import teamunc.uncsurvival.UNCSurvival;
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
        int itemPrice = UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemPrice(this.itemNumber-1);
        int itemNumbers = this.team.getItemsProduction(this.itemNumber-1);

        String itemPriceStr = this.reduceAt(21) + "       " + translateInInterfaceDisplay(""+ itemPrice,1);
        String itemNumberStr = this.reduceAt(4) + translateInInterfaceDisplay(""+ itemNumbers,4);

        this.inv = Bukkit.createInventory(null,27,name + itemPriceStr + itemNumberStr);
        return this.inv;
    }
}
