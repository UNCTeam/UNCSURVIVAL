package teamunc.uncsurvival.features.thirst;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ThirstActualiser {
    //# SINGLETON
    private static ThirstActualiser instance;
    private ThirstActualiser() {}
    public static ThirstActualiser getInstance() {
        if (ThirstActualiser.instance == null) ThirstActualiser.instance = new ThirstActualiser();
        return ThirstActualiser.instance;
    }
    //# END SINGLETON

    /**
     * from 0 to 20
     */
    private HashMap<String,Integer> thirstPerPlayerName = new HashMap<String,Integer>();

    public void registerPlayers(ArrayList<Player> players) {
        for (Player p : players) {
            this.thirstPerPlayerName.put(p.getName(),10);
        }
    }

    public void decreaseWater(int waterToRemove, String playerName) {
        int actualWater = this.thirstPerPlayerName.get(playerName);

        // set to 0 if lower than 0
        if (actualWater - waterToRemove < 0)
            thirstPerPlayerName.put(playerName,0);
        else
            thirstPerPlayerName.put(playerName,actualWater - 1);
    }

    public void decreaseWaterForAllRegisteredPlayers(int waterToRemove) {
        for (String playerName : this.thirstPerPlayerName.keySet()) {
            this.decreaseWater(1,playerName);
        }
    }

    public void increaseWater(int waterToAdd, String playerName) {
        int actualWater = this.thirstPerPlayerName.get(playerName);

        // set to 20 if higher than 20
        if (actualWater + waterToAdd > 20)
            thirstPerPlayerName.put(playerName,20);
        else
            thirstPerPlayerName.put(playerName,actualWater + 1);
    }

    public int getWaterLevel(String playerName) {
        return this.thirstPerPlayerName.get(playerName);
    }

    public void actualiseDisplay() {
        ThirstDisplay.getInstance().ActualiseDisplayForPlayers(this.thirstPerPlayerName);
    }

}
