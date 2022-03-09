package teamunc.uncsurvival.features.thirst;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.ArrayList;
import java.util.Arrays;

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


    public void registerPlayers(ArrayList<GamePlayer> players) {
        for (GamePlayer p : players) {
            p.setWaterLevel(18);
        }
    }

    public void decreaseWaterIfConnected(int waterToRemove, GamePlayer player) {
        if (player.isOnline()) {
            int actualWater = player.getWaterLevel();

            // set to 0 if lower than 0
            if ( actualWater - waterToRemove < 0 ) {
                player.setWaterLevel(0);
            } else
                player.setWaterLevel(actualWater - 1);
        }
    }

    public void damageAllnoWater() {
        for (GamePlayer player : this.gameManager.getParticipantManager().getGamePlayers()) {
            if (player.isOnline() && player.getWaterLevel() == 0) {
                player.getBukkitPlayer().damage(1);
            }
        }
    }

    public void decreaseWaterForAllRegisteredPlayers(int waterToRemove) {
        for (GamePlayer player : this.gameManager.getParticipantManager().getGamePlayers()) {
            this.decreaseWaterIfConnected(waterToRemove,player);
        }
    }

    /**
     * (i.e. waterPlayer = 10 + 14 from waterToAdd = 20 cause max and return true)
     * (i.e. waterPlayer = 20 + 14 from waterToAdd = 20 cause max and return false)
     * @param waterToAdd
     * @return false if water is full
     */
    public boolean increaseWater(int waterToAdd, GamePlayer player) {

        int actualWater = player.getWaterLevel();
        boolean res = true;
        if (actualWater == 20) {
            res = false;
        } else {
            // set to 20 if higher than 20
            if ( actualWater + waterToAdd > 20 )
                player.setWaterLevel(20);
            else
                player.setWaterLevel(actualWater + waterToAdd);
        }
        return res;
    }

    public void actualiseDisplay() {
        ThirstDisplay.getInstance().ActualiseDisplayForPlayers();
    }
}
