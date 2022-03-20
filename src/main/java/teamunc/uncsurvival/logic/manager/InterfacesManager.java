package teamunc.uncsurvival.logic.manager;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.GameCustomInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class InterfacesManager extends AbstractManager{

    private HashMap<Location, GameCustomInterface> interfaces = new HashMap<>();

    public InterfacesManager(UNCSurvival plugin) {
        super(plugin);
    }

    public ArrayList<String> getAllSymboles() {
        return new ArrayList<>(Arrays.asList(
                "本", "北", "被", "背", "备",
                "鼻",
                "杯", "抱", "帮"
        ));
    }

    public void addInterface(Location location,GameCustomInterface inventory) {
        this.interfaces.put(location,inventory);
    }

    public GameCustomInterface getInterface(Location location) {
        GameCustomInterface res = null;

        if(this.interfaces.containsKey(location)) {
            res = this.interfaces.get(location);
        }

        return res;
    }
    /**
     * workflow d'un block custom
     *
     * listener -> click droit sur un block -> prendre sa Location
     * sa Location -> check si Interface liée -> si oui alors l'ouvrir au joueur
     *                                           si non alors creer l'interface
     */
    public void ouvrirInterface(Location location, Player player) {
        GameCustomInterface inv = this.getInterface(location);
        if (inv != null) player.openInventory(inv.update());
    }



}
