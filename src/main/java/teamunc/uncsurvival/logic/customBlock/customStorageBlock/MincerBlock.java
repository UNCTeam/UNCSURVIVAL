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
        this.inventory = Bukkit.createInventory(null, 27, ChatColor.WHITE +"\uF80B杯");
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
    }

    public void checkIfCanProduce() {
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if(item != null && item.getType().equals(Material.BEEF)) {
                item.setAmount(item.getAmount()-1);
                duration = this.getProcessingDuration();
            }
        }
    }

    public void produceMincedMeat() {
        if(this.hasOutput()) {
            duration = -1;
            Hopper input = this.getOutput();
            Inventory outputInventory = input.getInventory();
            // Check si y a de la place
            if(outputInventory.firstEmpty() != -1) {
                outputInventory.addItem(UNCSurvival.getInstance().getGameManager().getItemsManager().createMincedMeat());
            } else {
                duration = 0;
            }
        }
    }

    public void fillFromInput() {
        if(!this.hasInput()) return;
        Hopper input = this.getInput();
        Inventory inputInventory = input.getInventory();
        for (int i = 0;i<input.getInventory().getSize();i++) {
            ItemStack item = inputInventory.getItem(i);
            if(item != null && item.getType() == Material.BEEF) {
                inventory.addItem(new ItemStack(Material.BEEF));
                item.setAmount(item.getAmount()-1);
            }
        }
    }
}
