package teamunc.uncsurvival.features.thirst;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.GameManager;

import java.util.ArrayList;
import java.util.HashMap;

public class ThirstActualiser {
    //# SINGLETON
    private static ThirstActualiser instance;
    private final GameManager gameManager;

    private ThirstActualiser() {
        this.gameManager = UNCSurvival.getInstance().getGameManager();
    }
    public static ThirstActualiser getInstance() {
        if (ThirstActualiser.instance == null) ThirstActualiser.instance = new ThirstActualiser();
        return ThirstActualiser.instance;
    }
    //# END SINGLETON

    /**
     * from 0 to 20
     */


    public void registerPlayers(ArrayList<Player> players) {
        for (Player p : players) {
            this.getThirstPerPlayerName().put(p.getName(),10);
        }
    }

    public void decreaseWaterIfConnected(int waterToRemove, String playerName) {
        Player p = Bukkit.getPlayerExact(playerName);
        if (p.isOnline()) {
            int actualWater = this.getThirstPerPlayerName().get(playerName);

            // set to 0 if lower than 0
            if ( actualWater - waterToRemove < 0 ) {
                this.getThirstPerPlayerName().put(playerName, 0);
            } else
                this.getThirstPerPlayerName().put(playerName, actualWater - 1);
        }
    }

    public void damageAllnoWater() {
        for (String playername : this.getThirstPerPlayerName().keySet()) {
            Player p = Bukkit.getPlayer(playername);
            if (p != null && this.getThirstPerPlayerName().get(playername) == 0) {
                p.damage(1);
            }
        }
    }

    public void decreaseWaterForAllRegisteredPlayers(int waterToRemove) {
        for (String playerName : this.getThirstPerPlayerName().keySet()) {
            this.decreaseWaterIfConnected(waterToRemove,playerName);
        }
    }

    /**
     * (i.e. waterPlayer = 10 + 14 from waterToAdd = 20 cause max and return true)
     * (i.e. waterPlayer = 20 + 14 from waterToAdd = 20 cause max and return false)
     * @param waterToAdd
     * @param playerName
     * @return false if water is full
     */
    public boolean increaseWater(int waterToAdd, String playerName) {
        int actualWater = this.getThirstPerPlayerName().get(playerName);
        boolean res = true;
        if (actualWater == 20) {
            res = false;
        } else {
            // set to 20 if higher than 20
            if ( actualWater + waterToAdd > 20 )
                this.getThirstPerPlayerName().put(playerName, 20);
            else
                this.getThirstPerPlayerName().put(playerName, actualWater + waterToAdd);
        }
        return res;
    }

    public void actualiseDisplay() {
        ThirstDisplay.getInstance().ActualiseDisplayForPlayers(this.getThirstPerPlayerName());
    }

    public HashMap<String, Integer> getThirstPerPlayerName() {
        return this.gameManager.getPlayersInformations().getThirstPerPlayerName();
    }
}
