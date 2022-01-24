package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

import java.util.Arrays;

public class MincerBlock extends CustomStorageBlock {

    public MincerBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);
        this.processingDuration = 10;
    }

    @Override
    public void tickAction() {
        // Vérifi si le duration == 0
        if(duration == 0) {
            produceMincedMeat();
        } else if(duration >= 0) {
            duration--;
        } else if(duration == -1) {
            checkIfCanProduce();
        }
        fillFromInput();
        exportOutput();
    }

    public void checkIfCanProduce() {
        ItemStack item = inventory.getItem(11);
        if(item != null && (item.getType().equals(Material.COOKED_BEEF) || item.getType().equals(Material.BEEF))) {
            item.setAmount(item.getAmount()-1);
            duration = this.getProcessingDuration();
        }
    }

    public void produceMincedMeat() {
        if(this.hasSpaceInOutput(Material.COOKED_BEEF)) {
            duration = -1;
            ItemStack item = this.inventory.getItem(15);
            if(item != null) {
                item.setAmount(item.getAmount()+1);
            } else {
                inventory.setItem(15, UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedMeat());
            }
        } else {
            duration = 0;
        }
    }

    public void exportOutput() {
        ItemStack output = this.inventory.getItem(15);
        if(output != null && this.hasOutput()) {
            Hopper input = this.getOutput();
            Inventory outputInventory = input.getInventory();
            // Check si y a de la place
            if (outputInventory.firstEmpty() != -1) {
                outputInventory.addItem(UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedMeat());
                output.setAmount(output.getAmount()-1);
            }
        }
    }

    public boolean hasSpaceInOutput(Material mat) {
        ItemStack item = inventory.getItem(15);
        if(item != null  && (item.getAmount() == 64 || !item.getType().equals(mat))) {
            return false;
        }
        return true;

    }

    public void fillFromInput() {
        if(!this.hasInput()) return;
        Hopper input = this.getInput();
        Inventory inputInventory = input.getInventory();
        for (int i = 0;i<input.getInventory().getSize();i++) {
            ItemStack item = inputInventory.getItem(i);
            if(item != null && item.getType() == Material.COOKED_BEEF) {
                if(inventory.getItem(11) != null && inventory.getItem(11).getAmount() != 64) {
                    inventory.getItem(11).setAmount(inventory.getItem(11).getAmount()+1);
                    item.setAmount(item.getAmount()-1);
                } else if(inventory.getItem(11) == null) {
                    Bukkit.broadcastMessage("else");
                    inventory.setItem(11, new ItemStack(Material.COOKED_BEEF));
                    item.setAmount(item.getAmount()-1);
                }
                return;
            }
        }
    }
}
