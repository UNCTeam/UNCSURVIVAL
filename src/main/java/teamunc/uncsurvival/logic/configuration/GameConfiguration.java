package teamunc.uncsurvival.logic.configuration;

import org.bukkit.Material;
import org.bukkit.entity.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GameConfiguration implements Serializable {
    private Date datePhase2;
    private Date datePhase3;
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;

    public GameConfiguration(Date datePhase2, Date datePhase3, ArrayList<Material> goalItems,ArrayList<Integer> goalItemsPrices) {
        this.datePhase2 = datePhase2;
        this.datePhase3 = datePhase3;

        this.goalItems = goalItems;
        this.goalItemsPrices = goalItemsPrices;

    }

    public Date getDatePhase2() {
        return datePhase2;
    }

    public Date getDatePhase3() {
        return datePhase3;
    }

    public ArrayList<Material> getGoalItems() {
        return goalItems;
    }
    public ArrayList<Integer> getGoalItemsPrices() {
        return goalItemsPrices;
    }
}
