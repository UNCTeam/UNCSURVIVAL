package teamunc.uncsurvival.logic.manager;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.IRecipe;
import org.bukkit.*;
import org.bukkit.block.BrewingStand;
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
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.MincerBlock;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.utils.alchemist.BrewingControler;
import teamunc.uncsurvival.utils.alchemist.BrewingRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ItemsManager extends AbstractManager {

    private NamespacedKey customitemKey = new NamespacedKey(this.plugin,"customitem");
    private NamespacedKey wrenchKey = new NamespacedKey(plugin, "wrenchID");
    private ArrayList<Integer> goalItemsPrices;
    private List<String> customItems;
    private BrewingControler brewingControler;
    private GameConfiguration gameConfiguration;

    public List<String> getCustomItems() {
        return customItems;
    }

    public NamespacedKey getCustomitemKey() {
        return customitemKey;
    }

    public NamespacedKey getWrenchKey() { return wrenchKey; }

    public ItemsManager(UNCSurvival plugin, GameConfiguration gameConfiguration) {
        super(plugin);
        this.gameConfiguration = gameConfiguration;
        this.gameConfiguration.setGoalItems(registerGoalItems());
        this.goalItemsPrices = gameConfiguration.getGoalItemsPrices();
        this.customItems = List.of("diamondApple", "wrench", "mincer", "healPatch", "alcool", "vaccin","module","mincedMeat","burger","wheatFlour","cactusJuice");
    }

    public ArrayList<ItemStack> registerGoalItems() {
        ArrayList<ItemStack> res = new ArrayList<>(
                Arrays.asList(
                        new ItemStack(Material.BRICK),
                        createBurger(),
                        new ItemStack(Material.BOOKSHELF),
                        createCactusJuice(),
                        new ItemStack(Material.STONE)
                )
        );

        return res;
    }

    public String getGoalItemName(Integer id) {
        return this.gameConfiguration.getGoalItems().get(id).getItemMeta().getDisplayName();
    }

    public ItemStack createDiamondApple() {
        ItemStack item = new ItemStack(Material.APPLE,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§r§l§cDiamond Apple");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"DIAMONDAPPLE");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createHealPatch() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(3);
        meta.setDisplayName("§rHeal Patch");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"HEALPATCH");
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
        data.set(this.customitemKey, PersistentDataType.STRING, "WRENCH");

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
        meta.setDisplayName("§rZombie Alcool");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"ALCOOL");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createVaccin() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(4);
        meta.setDisplayName("§b§cSARS-COV19 UNC-VAX");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"VACCIN");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createModule() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(5);
        meta.setDisplayName("§b§lUpgrade Region Module");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"MODULE");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createMincerItemBlock() {
        ItemStack item = new ItemStack(Material.DROPPER,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(CustomBlockType.MINCER_BLOCK.getModel());
        meta.setDisplayName("§rMincer");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, CustomBlockType.MINCER_BLOCK.name());
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createMincedMeat() {
        ItemStack item = new ItemStack(Material.COOKED_BEEF,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(2);
        meta.setDisplayName("§rMinced meat");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "MINCEDMEAT");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createBurger() {
        ItemStack item = new ItemStack(Material.COOKED_BEEF,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§b§eBurger");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "BURGER");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createWheatFlour() {
        ItemStack item = new ItemStack(Material.WHEAT,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§rWheat Flour");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "WHEATFLOUR");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createProgresBar(Integer pourcentage) {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(6);
        meta.setDisplayName("§a" + pourcentage + "%");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAnimatedMincer() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(7);
        meta.setDisplayName("§aProduction en cours");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createFixedMincer() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(8);
        meta.setDisplayName("§cProduction en pause");
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

    public ItemStack createCactusJuice() {
        ItemStack item = new ItemStack(Material.POTION,1);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setDisplayName("§b§eCactus Juice");
        meta.setColor(Color.fromRGB(902144));
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING,"CACTUS");
        item.setItemMeta(meta);
        return item;
    }

    public void initCraftingRecipe() {
        // potion craft manager
        this.brewingControler = new BrewingControler(this.plugin);

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

        // BURGER
        ShapedRecipe burger = new ShapedRecipe(new NamespacedKey(this.plugin,"craftBurger"),this.createBurger());
        burger.shape(" * "," - "," * ");
        burger.setIngredient('*',Material.BREAD);
        burger.setIngredient('-',new RecipeChoice.ExactChoice(this.createMincedMeat()));
        this.plugin.getServer().addRecipe(burger);

        // WHEAT FLOUR
        FurnaceRecipe wheat_flour = new FurnaceRecipe(new NamespacedKey(this.plugin,"craftWheatFlour"),this.createWheatFlour(),Material.WHEAT,1,200);
        this.plugin.getServer().addRecipe(wheat_flour);

        // BREAD
        FurnaceRecipe bread = new FurnaceRecipe(new NamespacedKey(this.plugin,"craftBread"),new ItemStack(Material.BREAD,1),new RecipeChoice.ExactChoice(this.createWheatFlour()),1,200);
        this.plugin.getServer().addRecipe(bread);

        // CACTUS JUICE
        // creating water potion
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.setBasePotionData(new PotionData(PotionType.WATER));
        potion.setItemMeta(potionMeta);
        brewingControler.addRecipe(
                new BrewingRecipe(new NamespacedKey(this.plugin,"craftCactusJuice"),this.createCactusJuice(),new ItemStack(Material.GREEN_DYE),potion,1,100)
        );

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

    public BrewingControler getBrewingControler() {
        return brewingControler;
    }

    public ItemStack getGoalItem(int itemNumber) {return this.gameConfiguration.getGoalItems().get(itemNumber);}

    public int getGoalItemPrice(Integer itemNumber) {

        return this.goalItemsPrices.get(itemNumber);
    }

    public boolean isCustomItem(ItemStack itemStack, String customNameCaseSensitive) {
        return (itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING) != null &&
                itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING).equals(customNameCaseSensitive));
    }

    public void save() {
        this.brewingControler.stop();
    }
}
