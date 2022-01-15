package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.GameInterfaceList;


public class InterfacesManager extends AbstractManager{

    private GameInterfaceList gameInterfaceList;

    public InterfacesManager(UNCSurvival plugin) {
        super(plugin);
        this.init();
    }

    public void init() {
    }

    /**
     * workflow d'un block custom
     *
     * listener -> click droit sur un block -> prendre sa Location
     * sa Location -> check si Interface liée -> si oui alors l'ouvrir au joueur
     *                                           si non alors creer l'interface
     */
    public void ouvrirInterface(Location location, Player player) {
        Inventory inv = this.gameInterfaceList.getInterface(location);

        if (inv == null) {
            inv = Bukkit.createInventory(null,27,"\uF80Bꈃ");
            this.gameInterfaceList.addInterface(location,inv);
        }

        player.openInventory(inv);
    }
}