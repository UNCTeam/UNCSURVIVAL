package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

import java.io.Serializable;
import java.util.Map;

public abstract class CustomStorageBlock implements Serializable {
    protected Location location;
    protected Inventory inventory;
    protected CustomBlockType customBlockType;
    protected int duration = -1;
    protected int processingDuration = 100;

    public CustomStorageBlock(Location location, CustomBlockType customBlockType) {
        this.customBlockType = customBlockType;
        this.location = location;
        if(customBlockType != CustomBlockType.BOTTLER_BLOCK) {
            location.getBlock().setType(Material.SMOOTH_STONE);
        }

        // Spawn Armorstand
        Location loc = location.clone().add(0.5,0,0.5);
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(customBlockType.getModel());
        texture.setItemMeta(textMeta);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorStand.addScoreboardTag("CUSTOM_BLOCK_"+location.getBlockX()+"_"+location.getBlockY()+"_"+location.getBlockZ());
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.getEquipment().setHelmet(texture);
    }

    public abstract void tickAction(int seconds);

    public Hopper getOutput() {
        if(this.location.clone().add(0,-1,0).getBlock().getType() == Material.HOPPER) {
            return (Hopper) this.location.clone().add(0,-1,0).getBlock().getState();
        }
        return null;
    }

    public Hopper getInput() {
        if(this.location.clone().add(0,1,0).getBlock().getType() == Material.HOPPER) {
            return (Hopper) this.location.clone().add(0,1,0).getBlock().getState();
        }
        return null;
    }

    protected void exportOutput(int indexItemToExport, ItemStack item) {
        ItemStack output = this.inventory.getItem(indexItemToExport);
        if(output != null && this.hasOutput() && output.isSimilar(item)) {
            Hopper input = this.getOutput();
            Inventory outputInventory = input.getInventory();
            // Check si y a de la place
            if (outputInventory.firstEmpty() != -1) {
                outputInventory.addItem(item);
                output.setAmount(output.getAmount()-1);
            }
        }
    }

    protected boolean hasSpaceInOutput(ItemStack itemToCheck, int index) {
        ItemStack item = inventory.getItem(index);
        if(item != null  && (item.getAmount() == 64 || !item.isSimilar(itemToCheck))) {
            return false;
        }
        return true;
    }

    protected boolean hasSpaceInOutput(Material mat, int index) {
        ItemStack item = inventory.getItem(index);
        if(item != null  && (item.getAmount() == 64 || !item.getType().equals(mat))) {
            return false;
        }
        return true;
    }

    public void moveItem(Integer itemIndex, ItemStack item) {
        if(inventory.getItem(itemIndex) != null && inventory.getItem(itemIndex).getAmount() != 64) {
            inventory.getItem(itemIndex).setAmount(inventory.getItem(itemIndex).getAmount()+1);
            item.setAmount(item.getAmount()-1);
        } else if(inventory.getItem(itemIndex) == null) {
            inventory.setItem(itemIndex, new ItemStack(item.getType()));
            item.setAmount(item.getAmount()-1);
        }
    }

    public boolean hasInput() {
        Block block = this.location.clone().add(0,1,0).clone().getBlock();
        return block.getType() == Material.HOPPER;
    }

    public boolean hasOutput() {
        Block block = this.location.clone().add(0,-1,0).clone().getBlock();
        return block.getType() == Material.HOPPER && !block.isBlockPowered();
    }

    public boolean isBlockLoaded() {
        return this.location.getChunk().isEntitiesLoaded();
    }

    public int getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingDuration(int processingDuration) {
        this.processingDuration = processingDuration;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public CustomBlockType getCustomBlockType() {
        return customBlockType;
    }

    public void setCustomBlockType(CustomBlockType customBlockType) {
        this.customBlockType = customBlockType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
