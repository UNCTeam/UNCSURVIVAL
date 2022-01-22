package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import teamunc.uncsurvival.logic.customBlock.CustomBlock;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

public abstract class CustomStorageBlock {
    private Location location;
    private Inventory inventory;
    private CustomBlockType customBlockType;
    private int duation = 100;

    public CustomStorageBlock(Location location, CustomBlockType customBlockType) {
        this.customBlockType = customBlockType;
        this.location = location;
        location.getBlock().setType(Material.SMOOTH_STONE);

        // Init l'inventaire
        this.inventory = Bukkit.createInventory(null, 27);

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

    public abstract void tickAction();

    public Hopper getOutput() {
        if(this.location.add(0,-1,0).getBlock().getType() == Material.HOPPER) {
            return (Hopper) this.location.add(0,-1,0).getBlock();
        }
        return null;
    }

    public Hopper getInput() {
        if(this.location.add(0,1,0).getBlock().getType() == Material.HOPPER) {
            return (Hopper) this.location.add(0,1,0).getBlock();
        }
        return null;
    }

    public boolean hasInput() {
        if(this.location.add(0,1,0).getBlock().getType() == Material.HOPPER) {
            return true;
        }
        return false;
    }

    public boolean hasOutput() {
        if(this.location.add(0,-1,0).getBlock().getType() == Material.HOPPER) {
            return true;
        }
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Inventory getInventory() {
        return inventory;
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

    public int getDuation() {
        return duation;
    }

    public void setDuation(int duation) {
        this.duation = duation;
    }
}
