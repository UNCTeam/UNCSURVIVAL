package teamunc.uncsurvival.logic.configuration;

import org.bukkit.Material;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GameConfiguration implements Serializable {
    private LocalDateTime datePhase2;
    private LocalDateTime datePhase3;
    private LocalDateTime dateFin;
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;

    public GameConfiguration(LocalDateTime datePhase2, LocalDateTime datePhase3, LocalDateTime dateFin,
                             ArrayList<Material> goalItems, ArrayList<Integer> goalItemsPrices) {
        this.datePhase2 = datePhase2;
        this.datePhase3 = datePhase3;
        this.dateFin = dateFin;
        this.goalItems = goalItems;
        this.goalItemsPrices = goalItemsPrices;
    }

    public LocalDateTime getDateFin() { return dateFin; }

    public LocalDateTime getDatePhase2() {
        return datePhase2;
    }

    public LocalDateTime getDatePhase3() {
        return datePhase3;
    }

    public ArrayList<Material> getGoalItems() {
        return goalItems;
    }
    public ArrayList<Integer> getGoalItemsPrices() {
        return goalItemsPrices;
    }
}
