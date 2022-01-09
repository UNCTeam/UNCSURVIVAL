package teamunc.uncsurvival.logic.manager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");

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
}
