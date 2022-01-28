package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.utils.Region;

public class GrowthBlock extends CustomStorageBlock {

    public GrowthBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);

        // Init l'inventaire
        this.inventory = Bukkit.createInventory(null, 27, UNCSurvival.getInstance().getGameManager().getCustomBlockManager().getTitle(this.customBlockType));
    }

    @Override
    public void tickAction() {

    }

    public Region getRegion() {
        return new Region(location, 4);
    }

    public void removeBoneMeal() {
        for(int i = 0;i<this.inventory.getSize();i++) {
            if(inventory.getItem(i) != null && inventory.getItem(i).getType() == Material.BONE_MEAL) {
                inventory.getItem(i).setAmount(inventory.getItem(i).getAmount()-1);
                return;
            }
        }
    }
}
