package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlock;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.CustomStorageBlock;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.GrowthBlock;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.MincerBlock;

import java.util.*;

public class CustomBlockManager extends AbstractManager {

    private HashMap<Location, CustomStorageBlock> customStorageBlockHashMap = new HashMap<>();
    private ArrayList<GrowthBlock> growthBlockArrayList = new ArrayList<>();

    public CustomBlockManager(UNCSurvival plugin) {
        super(plugin);
        this.loadCustomBlock();
    }

    public void actualiseBlocks() {
        for (Map.Entry<Location, CustomStorageBlock> mapentry: customStorageBlockHashMap.entrySet()) {
            mapentry.getValue().tickAction();
        }
    }

    public CustomStorageBlock getCustomBlock(Location location) {
        return this.customStorageBlockHashMap.get(location);
    }

    public ArrayList<GrowthBlock> getGrowthBlocks() {
        ArrayList<GrowthBlock> list = new ArrayList<>();
        for (Map.Entry<Location, CustomStorageBlock> mapentry: customStorageBlockHashMap.entrySet()) {
            if(mapentry.getValue() instanceof GrowthBlock) {
                list.add((GrowthBlock) mapentry.getValue());
            }
        }
        Bukkit.broadcastMessage("List of growth blocks : " + list.toString());
        return list;
    }

    public void addCustomBlock(CustomStorageBlock customBlock) {
        this.customStorageBlockHashMap.put(customBlock.getLocation(), customBlock);
        if(customBlock.getCustomBlockType() == CustomBlockType.GROWTH_BLOCK) {
            this.growthBlockArrayList.add((GrowthBlock) customBlock);
        }
    }

    public boolean removeCustomBlock(Location location) {
        if(this.customStorageBlockHashMap.containsKey(location)) {
            CustomStorageBlock customBlock = this.customStorageBlockHashMap.get(location);
            if(customBlock.getCustomBlockType() == CustomBlockType.GROWTH_BLOCK) {
                this.growthBlockArrayList.remove((GrowthBlock) customBlock);
            }
            this.customStorageBlockHashMap.remove(location);
            return true;
        }
        return false;
    }

    public void loadCustomBlock() {
        this.customStorageBlockHashMap = this.plugin.getFileManager().loadBlockManager();
        if(this.customStorageBlockHashMap == null) {
            this.customStorageBlockHashMap = new HashMap<>();
            Bukkit.broadcastMessage("aucun fichier");
        }
    }

    public void saveCustomBlock() {
        this.plugin.getFileManager().saveBlockManager(this.customStorageBlockHashMap);
    }

    public void breakCustomBlock(BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SMOOTH_STONE) return;

        CustomStorageBlock customBlock = this.getCustomBlock(event.getBlock().getLocation());

        if(customBlock == null) return;

        // Changement du loot

        event.setDropItems(false);
        event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(),
                this.getDrops(customBlock.getCustomBlockType()));

        for(ItemStack item : customBlock.getInventory().getContents()) {
            if(item != null) {
                event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(),
                        item); // DROP L'inventaire
            }
        }

        // Suppression armorstand

        Location loc = event.getBlock().getLocation();
        ArmorStand armorStand = (ArmorStand) Arrays.stream(loc.getWorld().getNearbyEntities(loc, 2, 2, 2, entity -> {
            return entity.getType().equals(EntityType.ARMOR_STAND)
                    && entity.getScoreboardTags().contains("CUSTOM_BLOCK_"+loc.getBlockX()+"_"+loc.getBlockY()+"_"+loc.getBlockZ());
        }).toArray()).findFirst().orElse(null);
        if(armorStand != null) {
            this.removeCustomBlock(loc);
            armorStand.remove();
        }
    }

    public ItemStack getDrops(CustomBlockType blockType) {
        ItemStack itemStack = null;
        switch (blockType) {
            case GROWTH_BLOCK:
                itemStack = plugin.getGameManager().getItemsManager().createGrowthItemBlock();
                break;
            case COOK_BLOCk:
                break;
            case MINCER_BLOCK:
                itemStack = plugin.getGameManager().getItemsManager().createMincerItemBlock();
                break;
            case PROECTION_BLOCK:
                break;
        }
        return itemStack;
    }

    public void loadInventoriesTitles() {
        for (Map.Entry<Location, CustomStorageBlock> entry : customStorageBlockHashMap.entrySet()) {
            Inventory newInv = Bukkit.createInventory(null, 27, this.getTitle(entry.getValue().getCustomBlockType()));
            newInv.setContents(entry.getValue().getInventory().getContents());
            entry.getValue().setInventory(newInv);
        }
    }

    public String getTitle(CustomBlockType blockType) {
        String name = "";
        switch (blockType) {
            case GROWTH_BLOCK:
                name = ChatColor.WHITE +"\uF80B抱";
                break;
            case COOK_BLOCk:
                break;
            case MINCER_BLOCK:
                name = ChatColor.WHITE +"\uF80B杯";
                break;
            case PROECTION_BLOCK:
                break;
        }
        return name;
    }

    public void interactBlockEvent(PlayerInteractEvent event) {
        if(event.getClickedBlock().getType() != Material.SMOOTH_STONE) return;

        CustomStorageBlock customBlock = this.getCustomBlock(event.getClickedBlock().getLocation());

        if(customBlock == null) return;

        event.getPlayer().openInventory(customBlock.getInventory());

    }

    public void placeCustomBlock(BlockPlaceEvent event) {
        if(event.getBlockAgainst().getType() == Material.SMOOTH_STONE) {
            if(!event.getPlayer().isSneaking()) {
                event.setCancelled(true);
            }
        }

        if(event.getItemInHand().getType() != Material.DROPPER) {
            return;
        }

        Location loc = event.getBlock().getLocation();

        ItemMeta itemMeta = event.getItemInHand().getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        if(data.has(plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING)) {
            CustomBlockType blockType = CustomBlockType.valueOf(data.get(plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING));
            switch (blockType) {
                case MINCER_BLOCK:
                    this.addCustomBlock(new MincerBlock(loc, blockType));
                    break;
                case COOK_BLOCk:
                    break;
                case PROECTION_BLOCK:
                    break;
                case GROWTH_BLOCK:
                    this.addCustomBlock(new GrowthBlock(loc, blockType));
                    break;
            }
        }
    }

    public void interfaceInterfact(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if(title.contains(this.getTitle(CustomBlockType.MINCER_BLOCK))) {
            if((event.getRawSlot() < 27 && (event.getRawSlot() != 11 && event.getRawSlot() != 15))) {
                event.setCancelled(true);
                return;
            } else if((event.getRawSlot() > 27 && event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) ||
                    event.getRawSlot() == 15 && event.getAction() == InventoryAction.PLACE_ALL) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
