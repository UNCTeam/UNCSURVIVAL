package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class TeamCustomInterface extends GameCustomInterface{
    public TeamCustomInterface() {
        super(6);
    }

    @Override
    public Inventory update() {
        // TODO interface de team
        this.inv = Bukkit.createInventory(null,27,name);
        return this.inv;
    }
}
