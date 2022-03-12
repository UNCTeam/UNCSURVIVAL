package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.data.Ageable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.utils.Region;

import java.util.Random;

public class GrowthBlock extends CustomStorageBlock {

    public GrowthBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);

        // Init l'inventaire
        this.inventory = Bukkit.createInventory(null, 27, UNCSurvival.getInstance().getGameManager().getCustomBlockManager().getTitle(this.customBlockType));
    }

    @Override
    public void tickAction(int seconds) {
        Location baseBlock = this.location.clone().add(-4, 0, -4);
        int isGrowing = 0;
        for(int i = 0; i<9;i++) {
            for(int j = 0;j<9;j++) {
                Block block = baseBlock.getBlock().getWorld().getBlockAt(baseBlock.getBlockX()+i, baseBlock.getBlockY(), baseBlock.getBlockZ()+j);
                if(block != null && block.getLocation().getChunk().isEntitiesLoaded()) {
                    isGrowing += checkIfGrowthable(block);
                }
            }
        }
        if((isGrowing > 0) && (seconds % 2 == 0)) {
            this.removeBoneMeal();
        }
        fillFromInput();
    }

    public void growBockAbove(Block block, Material mat, int size) {
        int age = 0;
        for(int i = 0; i<size; i++) {
            Block currentBlock = block.getLocation().clone().add(0, i, 0).getBlock();
            if (currentBlock.getType() == mat) {
                age++;
            }
        }
        if(age<4) {
            age=age+block.getY();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock " + block.getX() + " " + age + " " + block.getZ() + " minecraft:" + mat.name().toLowerCase());
        }
    }

    public int checkIfGrowthable(Block block) {
        if(this.inventory.contains(Material.BONE_MEAL)) {
            if (block.getBlockData() instanceof Ageable) {
                Random ran = new Random();
                int chance = ran.nextInt(10);
                if(chance == 0) {
                    if(block.getType() == Material.CACTUS) {
                        growBockAbove(block, Material.CACTUS, 3);
                        return 1;
                    } else if(block.getType() == Material.SUGAR_CANE) {
                        growBockAbove(block, Material.SUGAR_CANE, 3);
                        return 1;
                    } else if(block.getType() == Material.BAMBOO) {
                        growBockAbove(block, Material.BAMBOO, 12);
                            return 1;
                    } else {
                        Ageable ag = (Ageable) block.getBlockData();
                        int newAge = ag.getAge()+1;
                        if(newAge <= ag.getMaximumAge()) {
                            new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    ag.setAge(ag.getAge()+1);
                                    block.setBlockData(ag);
                                }
                            }.runTaskLater(UNCSurvival.getInstance(), 1);
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
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

    public void fillFromInput() {
        if(!this.hasInput()) return;
        Hopper input = this.getInput();
        Inventory inputInventory = input.getInventory();
        for (int i = 0;i<input.getInventory().getSize();i++) {
            ItemStack item = inputInventory.getItem(i);
            if(item != null && item.getType() == Material.BONE_MEAL) {
                    addInGrowthStorage(Material.BONE_MEAL);
                    item.setAmount(item.getAmount()-1);
                    return;
            }
        }
    }

    public void addInGrowthStorage(Material item) {
        for (int i = 3; i<22; i++) {
            if(inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemStack(item));
                return;
            } else if((inventory.getItem(i).getAmount() < 64) && (inventory.getItem(i).getType() == item)) {
                inventory.getItem(i).setAmount(inventory.getItem(i).getAmount()+1);
                return;
            }
            if(i == 5 || i == 14) {
                i+=7;
            }
        }

    }
}
