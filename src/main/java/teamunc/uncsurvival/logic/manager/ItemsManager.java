package teamunc.uncsurvival.logic.manager;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.IRecipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftShapelessRecipe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private NamespacedKey wrenchKey = new NamespacedKey(plugin, "wrenchID");
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;
    private ArrayList<String> customItems = new ArrayList<>();

    public ArrayList<String> getCustomItems() {
        return customItems;
    }

    public NamespacedKey getCustomitemKey() {
        return customitemKey;
    }

    public NamespacedKey getWrenchKey() { return wrenchKey; }

    public ItemsManager(UNCSurvival plugin, GameConfiguration gameConfiguration) {
        super(plugin);
        this.goalItems = gameConfiguration.getGoalItems();
        this.goalItemsPrices = gameConfiguration.getGoalItemsPrices();
        this.customItems.add("diamondApple");
        this.customItems.add("wrench");
        this.initCraftingRecipe();
    }

    public ItemStack createDiamondApple() {
        ItemStack item = new ItemStack(Material.APPLE,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(1);

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"DiamondApple");

        item.setItemMeta(meta);

        return item;
    }

    public String getGoalItemName(Integer id) {
        String res = "";
        switch (id) {
            case 0:
                res+="§f§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 1:
                res+="§6§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 2:
                res+="§2§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 3:
                res+="§3§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 4:
                res+="§9§b" + this.goalItems.get(id).name().replace("_"," ");
                break;
            case 5:
                res+="§5§b" + this.goalItems.get(id).toString();
                break;
        }
        return res;
    }

    public ItemStack createWrenchItem(Integer id, Integer durability) {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§8§lWrench - §f" + this.getGoalItemName(id) + " block");

        meta.setCustomModelData(2);

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.wrenchKey, PersistentDataType.INTEGER, id);

        item.setItemMeta(meta);

        // Change durability
        Damageable im = (Damageable) item.getItemMeta();
        int damage = durability;
        im.setDamage(damage);
        item.setItemMeta(im);

        return item;
    }

    public ItemStack giveNextWrenchItem(Integer id, Integer durability) {
        if(id != 4) {
            return this.createWrenchItem(id+1, durability);
        } else {
            return this.createWrenchItem(0, durability);
        }
    }

    public void initCraftingRecipe() {
        // DIAMOND APPLE
        ShapedRecipe diamondApple = new ShapedRecipe(new NamespacedKey(this.plugin,"craftDiamondApple"),this.createDiamondApple());
        diamondApple.shape("***","*-*","***");
        diamondApple.setIngredient('*',Material.DIAMOND);
        diamondApple.setIngredient('-',Material.GOLDEN_APPLE);
        this.plugin.getServer().addRecipe(diamondApple);

        // WRENCH
        ShapedRecipe wrench = new ShapedRecipe(new NamespacedKey(this.plugin,"craftWrench"),this.createWrenchItem(1,0));
        wrench.shape("/*/","/-*",".//");
        wrench.setIngredient('*',Material.IRON_INGOT);
        wrench.setIngredient('-',Material.REDSTONE);
        wrench.setIngredient('/',Material.AIR);
        wrench.setIngredient('.',Material.IRON_BLOCK);
        this.plugin.getServer().addRecipe(wrench);

        // change recipe
        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 || this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            this.replaceCraftPhase2();
            if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
                this.replaceCraftPhase3();
            }
        }

    }

    public void replaceCraftPhase2() {
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("oak_planks"),new ItemStack(Material.OAK_PLANKS,2)).addIngredient(Material.OAK_LOG),NamespacedKey.minecraft("oak_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("spruce_planks"),new ItemStack(Material.SPRUCE_PLANKS,2)).addIngredient(Material.SPRUCE_LOG),NamespacedKey.minecraft("spruce_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("birch_planks"),new ItemStack(Material.BIRCH_PLANKS,2)).addIngredient(Material.BIRCH_LOG),NamespacedKey.minecraft("birch_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("jungle_planks"),new ItemStack(Material.JUNGLE_PLANKS,2)).addIngredient(Material.JUNGLE_LOG),NamespacedKey.minecraft("jungle_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("acacia_planks"),new ItemStack(Material.ACACIA_PLANKS,2)).addIngredient(Material.ACACIA_LOG),NamespacedKey.minecraft("acacia_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("dark_oak_planks"),new ItemStack(Material.DARK_OAK_PLANKS,2)).addIngredient(Material.DARK_OAK_LOG),NamespacedKey.minecraft("dark_oak_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("crimson_planks"),new ItemStack(Material.CRIMSON_PLANKS,2)).addIngredient(Material.CRIMSON_STEM),NamespacedKey.minecraft("crimson_planks"));
        replaceCraft(new ShapelessRecipe(NamespacedKey.minecraft("warped_planks"),new ItemStack(Material.WARPED_PLANKS,2)).addIngredient(Material.WARPED_STEM),NamespacedKey.minecraft("warped_planks"));
    }

    public void replaceCraftPhase3() {

    }

    public void replaceCraft(Recipe recipe, NamespacedKey namespacedKey) {
        Bukkit.getServer().removeRecipe(namespacedKey);
        Bukkit.getServer().addRecipe(recipe);
    }

    public Material getItem(int itemNumber) {return this.goalItems.get(itemNumber);}

    public int getGoalItemPrice(Integer itemNumber) {

        return this.goalItemsPrices.get(itemNumber);
    }
}
