package teamunc.uncsurvival.logic.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.goals.GoalItem;

import java.util.ArrayList;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private ArrayList<GoalItem> goalItems = new ArrayList<>();

    public NamespacedKey getCustomitemKey() {
        return customitemKey;
    }

    public ItemsManager(UNCSurvival plugin) {
        super(plugin);
    }

    public ItemStack createDiamondApple() {
        ItemStack item = new ItemStack(Material.APPLE,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(1);

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"DiamondApple");

        item.setItemMeta(meta);

        return item;
    }

    public ArrayList<GoalItem> getGoalItems() {
        return goalItems;
    }

    public int getGoalItemScore(Material item) {
        int res = 0;

        for (GoalItem i : this.goalItems) {
            if (i.getItem() == item) {
                res = i.getPoints();
            }
        }

        return res;
    }

    public void setGoalItems(ArrayList<GoalItem> goalItems) {
        this.goalItems = goalItems;
    }
}
