package teamunc.uncsurvival.logic.player;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;

import java.io.Serializable;
import java.util.HashMap;

public class PlayersInformations implements Serializable {
    private static transient final long serialVersionUID = -1681012206529286330L;

    // Thirst level
    private HashMap<String,Integer> thirstPerPlayerName = new HashMap<String,Integer>();

    public HashMap<String, Integer> getThirstPerPlayerName() {
        return thirstPerPlayerName;
    }
}
