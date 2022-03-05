package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        location.getBlock().setType(Material.SMOOTH_STONE);

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

    public boolean hasInput() {
        return this.location.clone().add(0,1,0).clone().getBlock().getType() == Material.HOPPER;
    }

    public boolean hasOutput() {
        return this.location.clone().add(0,-1,0).clone().getBlock().getType() == Material.HOPPER;
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
