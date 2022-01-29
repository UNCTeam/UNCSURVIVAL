package teamunc.uncsurvival.utils.alchemist;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Hopper;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;
import teamunc.uncsurvival.UNCSurvival;

public class BrewingControler {
    private List<BrewingRecipe> recipes;
    private Listener potionEventListner;
    private ArrayList<Location> brewingStandLocations;
    private UNCSurvival plugin;

    public BrewingControler(UNCSurvival plugin) {
        recipes = new ArrayList<>();
        this.plugin = plugin;
        this.brewingStandLocations = plugin.getFileManager().loadBrewingStands();
        this.start(plugin);
    }
    
    public void start(Plugin plugin){
        this.stop();
        potionEventListner = new PotionEvent(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(potionEventListner, plugin);
    }

    public void stop(){
        if(potionEventListner != null){
            HandlerList.unregisterAll(potionEventListner);
            potionEventListner = null;
        }
        this.plugin.getFileManager().saveBrewingStands(this.brewingStandLocations);
    }

    public void addRecipe(BrewingRecipe recipe){
        this.recipes.add(recipe);
    }

    public void addStand(BrewingStand stand) {
        this.brewingStandLocations.add(stand.getLocation());
    }

    public void removeStand(Location standLocation) {
        this.brewingStandLocations.remove(standLocation);
    }

    public void checkHopperForBrewingStand() {
        for (Location loc :
                this.brewingStandLocations) {
            BrewingStand stand = (BrewingStand) loc.getBlock().getState();
            checkHopper(stand);
        }
    }

    private void checkHopper(BrewingStand stand) {
        Hopper input = getInput(stand);
        Inventory invStand = stand.getInventory();
        if (input != null) {
            Inventory inputInventory = input.getInventory();
            for (int i = 0;i<input.getInventory().getSize();i++) {
                ItemStack item = inputInventory.getItem(i);
                if(item != null && item.getType() == Material.GREEN_DYE) {
                    if(invStand.getItem(3) != null && invStand.getItem(3).getAmount() != 64) {
                        invStand.getItem(3).setAmount(invStand.getItem(3).getAmount()+1);
                        item.setAmount(item.getAmount()-1);
                    } else if(invStand.getItem(3) == null) {
                        invStand.setItem(3, new ItemStack(Material.GREEN_DYE));
                        item.setAmount(item.getAmount()-1);
                    }
                    return;
                }
            }
        }
    }

    public Hopper getInput(BrewingStand stand) {
        if(stand.getLocation().clone().add(0,1,0).getBlock().getType() == Material.HOPPER) {
            return (Hopper) stand.getLocation().clone().add(0,1,0).getBlock().getState();
        }
        return null;
    }

    public void removeRecipe(BrewingRecipe recipe){
        this.recipes.remove(recipe);
    }

    public BrewingRecipe getRecipe(NamespacedKey key){
        BrewingRecipe ret = null;
        for(BrewingRecipe recipe : this.recipes){
            if(recipe.getKey().equals(key)){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase){
        BrewingRecipe ret = null;
        for(BrewingRecipe recipe : this.recipes){
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if(rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount()
                && rBase.equals(inputBase)){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public BrewingRecipe getRecipe(ItemStack inputIngredient, ItemStack inputBase, int fuel){
        BrewingRecipe ret = null;

        for(BrewingRecipe recipe : this.recipes){
            ItemStack rIng = recipe.getInputIngredient();
            ItemStack rBase = recipe.getInputBase();
            if(rIng.isSimilar(inputIngredient) && rIng.getAmount() <= inputIngredient.getAmount()
                && rBase.equals(inputBase) && fuel >= recipe.getFuelUse()){
                ret = recipe;
                break;
            }
        }
        return ret;
    }

    public static int totalFuelInBrewingStand(BrewingStand stand){
        int total = stand.getFuelLevel();
        if(stand.getInventory().getFuel() != null && stand.getInventory().getFuel().getType() == Material.BLAZE_POWDER){
            total += 20 * stand.getInventory().getFuel().getAmount();
        }
        return total;
    }

    public List<BrewingRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<BrewingRecipe> recipes) {
        this.recipes = recipes;
    }

    public void clearRecipes(){
        this.recipes = new ArrayList<BrewingRecipe>();
    }
}
