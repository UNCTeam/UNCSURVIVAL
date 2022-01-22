package teamunc.uncsurvival.logic.manager;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.IRecipe;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftShapelessRecipe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.MincerBlock;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private NamespacedKey wrenchKey = new NamespacedKey(plugin, "wrenchID");
    private ArrayList<Material> goalItems;
    private ArrayList<Integer> goalItemsPrices;
    private List<String> customItems = new ArrayList<>();

    public List<String> getCustomItems() {
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
        this.customItems = List.of("diamondApple", "wrench", "mincer");
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

    public ItemStack createDiamondApple() {
        ItemStack item = new ItemStack(Material.APPLE,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(1);

        meta.setDisplayName("§bDiamond Apple");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"DiamondApple");

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createHealPatch() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(3);

        meta.setDisplayName("§bHeal Patch");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"HealPatch");

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createWrenchItem(Integer id, Integer durability) {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§8§lWrench - §f" + this.getGoalItemName(id) + " block");

        meta.setCustomModelData(2);

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.wrenchKey, PersistentDataType.INTEGER, id);
        data.set(this.customitemKey, PersistentDataType.STRING, "Wrench");

        item.setItemMeta(meta);

        // Change durability
        Damageable im = (Damageable) item.getItemMeta();
        int damage = durability;
        im.setDamage(damage);
        item.setItemMeta(im);

        return item;
    }

    public ItemStack createAlcool() {
        ItemStack item = new ItemStack(Material.POTION,1);

        PotionMeta meta = (PotionMeta) item.getItemMeta();

        meta.setColor(Color.BLACK);

        meta.setDisplayName("§bZombie Alcool");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING,"Alcool");

        item.setItemMeta(meta);

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

        // ALCOOL
        ShapedRecipe alcool = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAlcool"),this.createAlcool());
        alcool.shape("***","*-*","***");
        alcool.setIngredient('*',Material.ROTTEN_FLESH);
        alcool.setIngredient('-',Material.GLASS_BOTTLE);
        this.plugin.getServer().addRecipe(alcool);

        // HEAL PATCH
        ShapedRecipe heal_patch = new ShapedRecipe(new NamespacedKey(this.plugin,"craftHealPatch"),this.createHealPatch());
        heal_patch.shape("***","*-*","***");
        heal_patch.setIngredient('*',Material.PAPER);
        heal_patch.setIngredient('-',new RecipeChoice.ExactChoice(this.createAlcool()));
        this.plugin.getServer().addRecipe(heal_patch);

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

    public boolean isWrenchItem(ItemStack itemStack) {
        return (itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING) != null &&
                itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING).equals("Wrench"));
    }

    public boolean isDiamondAppleItem(ItemStack itemStack) {
        return (itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING) != null &&
                itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING).equals("DiamondApple"));
    }

    public ItemStack createMincerItemBlock() {
        ItemStack item = new ItemStack(Material.DROPPER,1);

        ItemMeta meta = item.getItemMeta();

        meta.setCustomModelData(CustomBlockType.MINCER_BLOCK.getModel());

        meta.setDisplayName("§bMincer");

        PersistentDataContainer data = meta.getPersistentDataContainer();

        data.set(this.customitemKey, PersistentDataType.STRING, CustomBlockType.MINCER_BLOCK.name());

        item.setItemMeta(meta);

        return item;
    }
}
