package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

public class BottlerBlock extends CustomStorageBlock {

    public BottlerBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);
        this.processingDuration = 20;
        this.inventory = Bukkit.createInventory(null, 27, UNCSurvival.getInstance().getGameManager().getCustomBlockManager().getTitle(this.customBlockType));
    }

    @Override
    public void tickAction(int seconds) {
        if(this.isBlockLoaded()) {
            if(duration == 0) {
                produceCactusJuce();
                resetProgressAnimation();
            } else if(duration > 0) {
                if(checkIfCanProduce()) {
                    updateProgressAnimation();
                    duration--;
                } else {
                    duration = this.getProcessingDuration();
                }
            } else if(duration == -1) {
                if(checkIfCanProduce()) {
                    duration = this.getProcessingDuration();
                }
            }
            fillFromInput();
            exportOutput(7, Material.GLASS_BOTTLE, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
            exportOutput(17, Material.GLASS_BOTTLE, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
            exportOutput(25, Material.GLASS_BOTTLE, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
        }
    }

    private void updateProgressAnimation() {
        double pourcentage = (1 - (double) duration / processingDuration) * 100;
        ItemStack progress = UNCSurvival.getInstance().getGameManager().getItemsManager().createProgresBarBottler((int) pourcentage);
        inventory.setItem(14, progress);
    }

    private void resetProgressAnimation() {
        ItemStack progress = UNCSurvival.getInstance().getGameManager().getItemsManager().createProgresBarBottler(0);
        inventory.setItem(14, progress);
    }

    private void fillFromInput() {
        if(!this.hasInput()) return;
        Hopper input = this.getInput();
        Inventory inputInventory = input.getInventory();
        for (int i = 0;i<input.getInventory().getSize();i++) {
            ItemStack item = inputInventory.getItem(i);
            if(item != null && item.getType() == Material.BLAZE_POWDER) {
                this.moveItem(10, item);
            } else if(item != null && item.getType() == Material.GREEN_DYE) {
                this.moveItem(12, item);
            } else if(item != null && item.getType() == Material.GLASS_BOTTLE) {
                this.checkIfNullThenFill(7, item);
                this.checkIfNullThenFill(17, item);
                this.checkIfNullThenFill(25, item);
            }
        }
    }

    private void checkIfNullThenFill(int index, ItemStack item) {
        if(inventory.getItem(index) == null) {
            inventory.setItem(index, new ItemStack(item.getType()));
            item.setAmount(item.getAmount()-1);
        }
    }

    private boolean checkIfCanProduce() {
        ItemStack powder = inventory.getItem(10);
        ItemStack dye = inventory.getItem(12);
        ItemStack bottle1 = inventory.getItem(7);
        ItemStack bottle2 = inventory.getItem(17);
        ItemStack bottle3 = inventory.getItem(25);
        if(powder != null && powder.getType().equals(Material.BLAZE_POWDER) && dye != null && dye.getType().equals(Material.GREEN_DYE)
        && bottle1 != null && bottle1.getType().equals(Material.GLASS_BOTTLE) && bottle2 != null && bottle2.getType().equals(Material.GLASS_BOTTLE)
        && bottle3 != null && bottle3.getType().equals(Material.GLASS_BOTTLE)) {
            return true;
        }
        return false;
    }

    private void produceCactusJuce() {
        duration = -1;
        inventory.getItem(10).setAmount(inventory.getItem(10).getAmount());
        inventory.getItem(12).setAmount(inventory.getItem(12).getAmount());
        inventory.setItem(7, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
        inventory.setItem(17, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
        inventory.setItem(25, UNCSurvival.getInstance().getGameManager().getItemsManager().createCactusJuice());
    }
}
