package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class GoalCustomInterface extends GameCustomInterface{

    /**
     *
     * @param itemNumber le nombre item goal (a voir sur le trello)
     */
    public GoalCustomInterface(int itemNumber) {
        super(itemNumber);
    }

    public Inventory updateName(int itemPrice, int itemNumber) {
        String itemPriceStr = this.reduceAt(21) + "       " + translateInInterfaceDisplay(""+ itemPrice,1);
        String itemNumberStr = this.reduceAt(4) + translateInInterfaceDisplay(""+ itemNumber,4);

        this.inv = Bukkit.createInventory(null,27,name + itemPriceStr + itemNumberStr);
        return this.inv;
    }
}
