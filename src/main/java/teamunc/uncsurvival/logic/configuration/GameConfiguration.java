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

    public void postLoad() {
        this.goalItems = registerGoalItems();
    }

    public ArrayList<ItemStack> registerGoalItems() {
        ArrayList<ItemStack> res = new ArrayList<>(
                Arrays.asList(
                        new ItemStack(Material.BRICK),
                        createBurger(),
                        new ItemStack(Material.STONE),
                        new ItemStack(Material.STONE),
                        new ItemStack(Material.STONE)
                )
        );

        return res;
    }

    private ItemStack createBurger() {
        ItemStack item = new ItemStack(Material.COOKED_BEEF,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(1);

        meta.setDisplayName("§b§eBurger");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(new NamespacedKey(UNCSurvival.getInstance(),"customitem"), PersistentDataType.STRING, "BURGER");

        item.setItemMeta(meta);

        return item;
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
}
