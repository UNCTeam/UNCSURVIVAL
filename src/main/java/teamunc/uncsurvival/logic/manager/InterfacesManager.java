package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.GameInterfaceList;
import teamunc.uncsurvival.logic.interfaces.GoalCustomInterface;


public class InterfacesManager extends AbstractManager{

    private GameInterfaceList gameInterfaceList = new GameInterfaceList();


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

        if (inv != null) player.openInventory(inv);
    }



}
