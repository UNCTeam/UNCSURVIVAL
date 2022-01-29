package teamunc.uncsurvival.logic.configuration;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class GameConfiguration implements Serializable {
    private LocalDateTime datePhase2;
    private LocalDateTime datePhase3;
    private LocalDateTime dateFin;
    private transient ArrayList<ItemStack> goalItems;
    private ArrayList<Integer> goalItemsPrices;

    public GameConfiguration(LocalDateTime datePhase2, LocalDateTime datePhase3, LocalDateTime dateFin,
                              ArrayList<Integer> goalItemsPrices) {
        this.datePhase2 = datePhase2;
        this.datePhase3 = datePhase3;
        this.dateFin = dateFin;
        this.goalItemsPrices = goalItemsPrices;
    }

    public LocalDateTime getDateFin() { return dateFin; }

    public LocalDateTime getDatePhase2() {
        return datePhase2;
    }

    public LocalDateTime getDatePhase3() {
        return datePhase3;
    }

    public ArrayList<ItemStack> getGoalItems() {
        return goalItems;
    }
    public ArrayList<Integer> getGoalItemsPrices() {
        return goalItemsPrices;
    }

    public void setGoalItems(ArrayList<ItemStack> registeredGoalItems) {
        this.goalItems = registeredGoalItems;
    }
}
