package teamunc.uncsurvival.logic.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;

import java.util.ArrayList;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private NamespacedKey wrenchKey = new NamespacedKey(plugin, "wrenchID");
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;
    private ArrayList<String> customItems = new ArrayList<>();

    public ArrayList<String> getCustomItems() {
        return customItems;
    }

    public NamespacedKey getCustomitemKey() {
        return customitemKey;
    }

    public NamespacedKey getWrenchKey() { return wrenchKey; }

    public ItemsManager(UNCSurvival plugin, GameConfiguration gameConfiguration) {
        super(plugin);
        this.goalItems = gameConfiguration.getGoalItems();
        this.goalItemsPrices = gameConfiguration.getGoalItemsPrices();
        this.customItems.add("diamondApple");
        this.customItems.add("wrench");
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

    public String getGoalItemName(Integer id) {
        String res = "";
        switch (id) {
            case 0:
                res+="§f§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 1:
                res+="§6§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 2:
                res+="§2§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 3:
                res+="§3§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 4:
                res+="§9§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 5:
                res+="§5§b" + this.goalItems.get(id).toString();
                break;
        }
        return res;
    }

    public ItemStack createWrenchItem(Integer id, Integer durability) {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§8§lWrench - §f" + this.getGoalItemName(id) + " block");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"DiamondApple");
        data.set(this.wrenchKey, PersistentDataType.INTEGER, id);

        item.setItemMeta(meta);

        // Change durability
        Damageable im = (Damageable) item.getItemMeta();
        int damage = durability;
        im.setDamage(damage);
        item.setItemMeta(im);

        return item;
    }

    public ItemStack giveNextWrenchItem(Integer id, Integer durability) {
        if(id != 4) {
            return this.createWrenchItem(id+1, durability);
        } else {
            return this.createWrenchItem(0, durability);
        }
    }

    public Material getItem(int itemNumber) {return this.goalItems.get(itemNumber);}

    public int getGoalItemPrice(Integer itemNumber) {

        return this.goalItemsPrices.get(itemNumber);
    }
}
