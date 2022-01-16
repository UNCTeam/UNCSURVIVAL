package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class GameInterfaceList {
    private HashMap<Location, Inventory> interfaces = new HashMap<>();

    public GameInterfaceList() {}

    public HashMap<Location, Inventory> getInterfaces() {
        return interfaces;
    }

    public void addInterface(Location location,Inventory inventory) {
        this.interfaces.put(location,inventory);
    }

    public Inventory getInterface(Location location) {
        Inventory res = null;

        if(this.interfaces.containsKey(location)) {
            res = this.interfaces.get(location);
        }

        return res;
    }
}
