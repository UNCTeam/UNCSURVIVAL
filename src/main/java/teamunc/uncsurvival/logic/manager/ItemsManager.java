package teamunc.uncsurvival.logic.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;

import java.util.ArrayList;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;

    public NamespacedKey getCustomitemKey() {
        return customitemKey;
    }

    public ItemsManager(UNCSurvival plugin, GameConfiguration gameConfiguration) {
        super(plugin);
        this.goalItems = gameConfiguration.getGoalItems();
        this.goalItemsPrices = gameConfiguration.getGoalItemsPrices();
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

    public Material getItem(int itemNumber) {return this.goalItems.get(itemNumber);}

    public int getGoalItemPrice(Integer itemNumber) {

        return this.goalItemsPrices.get(itemNumber);
    }
}
