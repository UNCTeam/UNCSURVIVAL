package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

public class MincerBlock extends CustomStorageBlock {

    private String itemToProduce = "";

    public MincerBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);
        this.processingDuration = 10;

        // Init l'inventaire
        this.inventory = Bukkit.createInventory(null, 27, UNCSurvival.getInstance().getGameManager().getCustomBlockManager().getTitle(this.customBlockType));
        inventory.setItem(4, UNCSurvival.getInstance().getGameManager().getItemsManager().createFixedMincer());
    }

    @Override
    public void tickAction(int seconds) {
        if(this.isBlockLoaded()) {
            if(duration == 0) {
                produce();
            } else if(duration > 0) {
                updateProgressBar();
                duration--;
            } else if(duration == -1) {
                checkIfCanProduce();
            }
            fillFromInput();

            exportOutput(15, this.getInventory().getItem(15));
        }
    }

    public void checkIfCanProduce() {
        ItemStack item = inventory.getItem(11);
        if(item != null && (item.getType().equals(Material.COOKED_BEEF) || item.getType().equals(Material.BEEF)) ) {
            itemToProduce = "MincedMeat";
            item.setAmount(item.getAmount()-1);
            duration = this.getProcessingDuration();
            this.inventory.setItem(4,UNCSurvival.getInstance().getGameManager().getItemsManager().createAnimatedMincer());
        } else if (item != null && item.getType().equals(Material.DRIED_KELP_BLOCK) ) {
            itemToProduce = "MincedTofuMeat";
            item.setAmount(item.getAmount()-1);
            duration = this.getProcessingDuration();
            this.inventory.setItem(4,UNCSurvival.getInstance().getGameManager().getItemsManager().createAnimatedMincer());
        }
    }

    public void updateProgressBar() {
        double pourcentage = (1 - (double) duration / processingDuration) * 100;
        ItemStack progress = UNCSurvival.getInstance().getGameManager().getItemsManager().createProgresBar((int) pourcentage);
        int nb = 5 - (duration / (processingDuration / 5));
        for(int i = 0; i<nb; i++) {
            inventory.setItem(20+i, progress);
        }
    }

    public void clearProgressBar() {
        for(int i = 0; i<5; i++) {
            inventory.setItem(20+i, null);
        }
    }

    public void produce() {
        if(this.hasSpaceInOutput(UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedMeat(), 15) && this.itemToProduce == "MincedMeat") {
            this.itemToProduce = "";
            duration = -1;
            ItemStack item = this.inventory.getItem(15);
            if(item != null) {
                item.setAmount(item.getAmount()+1);
            } else {
                inventory.setItem(15, UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedMeat());
            }
            clearProgressBar();
            inventory.setItem(4, UNCSurvival.getInstance().getGameManager().getItemsManager().createFixedMincer());
        } else if (this.hasSpaceInOutput(UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedTofuMeat(), 15) && this.itemToProduce == "MincedTofuMeat"){
            this.itemToProduce = "";
            duration = -1;
            ItemStack item = this.inventory.getItem(15);
            if(item != null) {
                item.setAmount(item.getAmount()+1);
            } else {
                inventory.setItem(15, UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedTofuMeat());
            }
            clearProgressBar();
            inventory.setItem(4, UNCSurvival.getInstance().getGameManager().getItemsManager().createFixedMincer());
        } else {
            duration = 0;
        }
    }

    public void fillFromInput() {
        if(!this.hasInput()) return;
        Hopper input = this.getInput();
        Inventory inputInventory = input.getInventory();
        for (int i = 0;i<input.getInventory().getSize();i++) {
            ItemStack item = inputInventory.getItem(i);
            if(item != null && (item.getType() == Material.COOKED_BEEF || item.getType() == Material.BEEF || item.getType() == Material.DRIED_KELP_BLOCK)) {
                this.moveItem(11, item);
                return;
            }
        }
    }
}
