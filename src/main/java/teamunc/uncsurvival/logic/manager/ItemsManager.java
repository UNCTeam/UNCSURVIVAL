package teamunc.uncsurvival.logic.manager;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.world.item.crafting.IRecipe;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BrewingStand;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftShapelessRecipe;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.MincerBlock;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.utils.alchemist.BrewingControler;
import teamunc.uncsurvival.utils.alchemist.BrewingRecipe;

import java.util.*;

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

    public ItemsManager(UNCSurvival plugin, GameConfiguration gameConfiguration, GameStats gameStats) {
        super(plugin);
        this.gameConfiguration = gameConfiguration;
        registerGoalItems(gameStats.getCurrentPhase());
        this.goalItemsPrices = gameConfiguration.getGoalItemsPrices();
        this.customItems = List.of("diamondApple", "wrench", "mincer", "healPatch", "alcool",
                "vaccin","module","mincedMeat","burger","wheatFlour", "growthBlock", "cactusJuice","amethystIngot","amethystSword","amethystPickaxe","famineSoup");
    }

    public void registerGoalItems(PhaseEnum phase) {
        ArrayList<ItemStack> res = new ArrayList<>(
                Arrays.asList(
                        new ItemStack(Material.PUMPKIN_PIE),
                        createBurger(),
                        new ItemStack(Material.BOOKSHELF),
                        createCactusJuice()
                )
        );

        switch (phase) {

            case INIT:
            case LANCEMENT:
            case PHASE1:
                res.add(new ItemStack(Material.WHITE_WOOL));
                break;
            case PHASE2:
                res.add(new ItemStack(Material.OBSIDIAN));
                break;
            case PHASE3:
            case FIN:
                res.add(new ItemStack(Material.RED_MUSHROOM_BLOCK));
                break;
        }

        this.gameConfiguration.setGoalItems(res);
    }

    public String getGoalItemName(Integer id) {
        String res = "§b§e";
        if (this.gameConfiguration.getGoalItems().get(id).getItemMeta().hasDisplayName()) {
            res += this.gameConfiguration.getGoalItems().get(id).getItemMeta().getDisplayName();
        } else {
            res += this.gameConfiguration.getGoalItems().get(id).getType().name().toLowerCase(Locale.ROOT);
        }
        return res;
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
        meta.setLore(List.of("§r§8Qualité : §r§l0"));
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

    public ItemStack createGrowthItemBlock() {
        ItemStack item = new ItemStack(Material.DROPPER,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(CustomBlockType.GROWTH_BLOCK.getModel());
        meta.setDisplayName("§aGrowth block");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, CustomBlockType.GROWTH_BLOCK.name());
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

    public ItemStack createFamineSoup() {
        ItemStack item = new ItemStack(Material.CARROT_ON_A_STICK,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(9);
        meta.setDisplayName("§rFamine Soup");
        meta.setLore(List.of("§r§cUn remède puissant et très curatif de grand-mère..."));
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "FAMINESOUP");
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

    public ItemStack createAmethystIngot() {
        ItemStack item = new ItemStack(Material.STRUCTURE_BLOCK,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setDisplayName("§rAmethyst Ingot");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTINGOT");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAmethystHelmet() {
        ItemStack item = new ItemStack(Material.LEATHER_HELMET,1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setColor(Color.fromRGB(16777215));

        meta.setLore(List.of("§rGive a bit of speed and a heart"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"generic.max_health",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.movement_speed",0.01, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"generic.armor",3, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"generic.armor",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HEAD));

        meta.setDisplayName("§rAmethyst Helmet");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTHELMET");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAmethystChestPlate() {
        ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE,1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setColor(Color.fromRGB(16777215));

        meta.setLore(List.of("§rGive a bit of speed and a heart"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"generic.max_health",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.CHEST));
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.movement_speed",0.01, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.CHEST));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"generic.armor",8, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.CHEST));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"generic.armor",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.CHEST));

        meta.setDisplayName("§rAmethyst ChestPlate");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTCHESTPLATE");
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack createAmethystLeggings() {
        ItemStack item = new ItemStack(Material.LEATHER_LEGGINGS,1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setColor(Color.fromRGB(16777215));

        meta.setLore(List.of("§rGive a bit of speed and a heart"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"generic.max_health",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.LEGS));
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.movement_speed",0.01, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.LEGS));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"generic.armor",6, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.LEGS));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"generic.armor",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.LEGS));

        meta.setDisplayName("§rAmethyst Leggings");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTLEGGINGS");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAmethystBoots() {
        ItemStack item = new ItemStack(Material.LEATHER_BOOTS,1);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setCustomModelData(1);
        meta.setColor(Color.fromRGB(16777215));

        meta.setLore(List.of("§rGive a bit of speed and a heart"));
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"generic.max_health",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.movement_speed",0.01, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(UUID.randomUUID(),"generic.armor",3, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.FEET));
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(UUID.randomUUID(),"generic.armor",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.FEET));

        meta.setDisplayName("§rAmethyst Boots");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTBOOTS");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAmethystSword() {
        ItemStack item = new ItemStack(Material.NETHERITE_SWORD,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);

        meta.setLore(List.of("§b§oThe power in your hand..."));
        meta.setUnbreakable(true);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(),"generic.attack_damage",12, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(UUID.randomUUID(),"generic.attack_damage",2, AttributeModifier.Operation.ADD_NUMBER,EquipmentSlot.HAND));

        meta.setDisplayName("§cAmethyst Sword");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTSWORD");
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createAmethystPickaxe() {
        ItemStack item = new ItemStack(Material.NETHERITE_PICKAXE,1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);

        meta.setLore(List.of("§b§oThe Ultimate Tool..."));
        meta.setUnbreakable(true);

        meta.setDisplayName("§cAmethyst Pickaxe");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customitemKey, PersistentDataType.STRING, "AMETHYSTPICKAXE");
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

        // ALCOOL QUALITY
        ShapedRecipe alcoolQuality = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAlcoolQuality"),this.createAlcool());
        alcoolQuality.shape("***","*-*","***");
        alcoolQuality.setIngredient('*',Material.ROTTEN_FLESH);
        alcoolQuality.setIngredient('-',Material.POTION);
        this.plugin.getServer().addRecipe(alcoolQuality);

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

        // AMETHYST INGOT
        ShapedRecipe amethystIngot = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAmethystIngot"),this.createAmethystIngot());
        amethystIngot.shape("*-*","*-*");
        amethystIngot.setIngredient('*',Material.AMETHYST_BLOCK);
        amethystIngot.setIngredient('-',Material.DIAMOND);
        this.plugin.getServer().addRecipe(amethystIngot);

        // AMETHYST ARMOR
        // HELMET
        ShapedRecipe amethystHelmet = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAmethystHelmet"),this.createAmethystHelmet());
        amethystHelmet.shape("***","* *");
        amethystHelmet.setIngredient('*',new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        // LEGGING
        ShapedRecipe amethystLegging = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAmethystLeggings"),this.createAmethystLeggings());
        amethystLegging.shape("***","* *","* *");
        amethystLegging.setIngredient('*',new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        // CHESTPLATE
        ShapedRecipe amethystChestPlate = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAmethystChestPlate"),this.createAmethystChestPlate());
        amethystChestPlate.shape("* *","***","***");
        amethystChestPlate.setIngredient('*',new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        // BOOTS
        ShapedRecipe amethystBoots = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAmethystBoots"),this.createAmethystBoots());
        amethystBoots.shape("* *","* *");
        amethystBoots.setIngredient('*',new RecipeChoice.ExactChoice(this.createAmethystIngot()));

        this.plugin.getServer().addRecipe(amethystHelmet);
        this.plugin.getServer().addRecipe(amethystChestPlate);
        this.plugin.getServer().addRecipe(amethystLegging);
        this.plugin.getServer().addRecipe(amethystBoots);

        // BREAD
        FurnaceRecipe bread = new FurnaceRecipe(new NamespacedKey(this.plugin,"craftBread"),new ItemStack(Material.BREAD,1),new RecipeChoice.ExactChoice(this.createWheatFlour()),1,200);
        this.plugin.getServer().addRecipe(bread);

        // MINCER
        ShapedRecipe mincer = new ShapedRecipe(new NamespacedKey(this.plugin,"craftMincer"),this.createMincerItemBlock());
        mincer.shape("/*/","*^*","/-/");
        mincer.setIngredient('*',Material.SHEARS);
        mincer.setIngredient('/',Material.STONE);
        mincer.setIngredient('^',Material.IRON_BLOCK);
        mincer.setIngredient('-',Material.REDSTONE);
        this.plugin.getServer().addRecipe(mincer);

        // GROWTH
        ShapedRecipe growth = new ShapedRecipe(new NamespacedKey(this.plugin,"craftGrowth"),this.createGrowthItemBlock());
        growth.shape("***","^-^","///");
        growth.setIngredient('*',Material.IRON_INGOT);
        growth.setIngredient('/',new RecipeChoice.MaterialChoice(Material.JUNGLE_PLANKS,
                Material.ACACIA_PLANKS,
                Material.BIRCH_PLANKS,
                Material.CRIMSON_PLANKS,
                Material.OAK_PLANKS,
                Material.SPRUCE_PLANKS,
                Material.DARK_OAK_PLANKS,
                Material.WARPED_PLANKS
        ));
        growth.setIngredient('^',Material.COMPOSTER);
        growth.setIngredient('-',Material.REDSTONE_BLOCK);
        this.plugin.getServer().addRecipe(growth);

        // Module
        ShapedRecipe module = new ShapedRecipe(new NamespacedKey(this.plugin,"craftModule"),this.createModule());
        module.shape("*^*","^-^","*/*");
        module.setIngredient('*',Material.COPPER_INGOT);
        module.setIngredient('/',Material.GOLD_INGOT);
        module.setIngredient('^',Material.DIAMOND);
        module.setIngredient('-',Material.REDSTONE_TORCH);
        this.plugin.getServer().addRecipe(module);

        // FAMINE SOUP
        ShapedRecipe famineSoup = new ShapedRecipe(new NamespacedKey(this.plugin,"craftFamineSoup"),this.createFamineSoup());
        famineSoup.shape(" ^ ","*-_"," / ");
        famineSoup.setIngredient('*',Material.GLOW_INK_SAC);
        famineSoup.setIngredient('/',new RecipeChoice.ExactChoice(this.createBurger()));
        famineSoup.setIngredient('^',Material.GOLDEN_CARROT);
        famineSoup.setIngredient('-',Material.BOWL);
        famineSoup.setIngredient('_',Material.FERMENTED_SPIDER_EYE);
        this.plugin.getServer().addRecipe(famineSoup);

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
        replaceCraft(
                new ShapedRecipe(
                        NamespacedKey.minecraft("amethyst_block"),
                        new ItemStack(Material.AMETHYST_BLOCK,1))
                        .shape("***","***","***")
                        .setIngredient('*',Material.AMETHYST_SHARD),
                NamespacedKey.minecraft("amethyst_block")
        );

        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() != PhaseEnum.PHASE1) {
            this.replaceCraftPhase2();
            if (this.plugin.getGameManager().getGameStats().getCurrentPhase() != PhaseEnum.PHASE2) {
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
        // AMETHYST SWORD
        ShapelessRecipe amethystSword = new ShapelessRecipe(new NamespacedKey(this.plugin,"craftAmethystSword"),this.createAmethystSword());
        amethystSword.addIngredient(new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        amethystSword.addIngredient(new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        amethystSword.addIngredient(Material.DIAMOND_SWORD);
        this.plugin.getServer().addRecipe(amethystSword);

        // AMETHYST PICKAXE
        ShapelessRecipe amethystPickaxe = new ShapelessRecipe(new NamespacedKey(this.plugin,"craftAmethystPickaxe"),this.createAmethystPickaxe());
        amethystPickaxe.addIngredient(new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        amethystPickaxe.addIngredient(new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        amethystPickaxe.addIngredient(new RecipeChoice.ExactChoice(this.createAmethystIngot()));
        amethystPickaxe.addIngredient(Material.DIAMOND_PICKAXE);
        this.plugin.getServer().addRecipe(amethystPickaxe);
    }

    public void replaceCraft(Recipe recipe, NamespacedKey namespacedKey) {
        Bukkit.getServer().removeRecipe(namespacedKey);
        Bukkit.getServer().addRecipe(recipe);
    }

    public BrewingControler getBrewingControler() {
        return brewingControler;
    }

    public ItemStack getGoalItem(int itemNumber) {return this.gameConfiguration.getGoalItems().get(itemNumber);}

    public int getGoalItemPrice(Integer itemNumber,PhaseEnum phase) {
        int res = 0;
        if (itemNumber == 4) {
            switch (phase) {
                case INIT: case LANCEMENT: case PHASE1:
                    res = this.goalItemsPrices.get(4);
                    break;
                case PHASE2:
                    res = this.goalItemsPrices.get(5);
                    break;
                case PHASE3: case FIN:
                    res = this.goalItemsPrices.get(6);
                    break;
            }
        } else res = this.goalItemsPrices.get(itemNumber);

        return res;
    }

    public int getGoalItemPrice(Integer itemNumber) {
        PhaseEnum phase = UNCSurvival.getInstance().getGameManager().getGameStats().getCurrentPhase();
        return getGoalItemPrice(itemNumber,phase);
    }

    public boolean isCustomItem(ItemStack itemStack, String customNameCaseSensitive) {
        return (itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING) != null &&
                itemStack.getItemMeta().getPersistentDataContainer().get(this.getCustomitemKey(),PersistentDataType.STRING).equals(customNameCaseSensitive));
    }

    public void save() {
        this.brewingControler.stop();
    }

    public Set<Material> getAllFoodOfTheGame() {
        Set<Material> res = new HashSet<>();

        res.addAll(List.of(
                Material.GOLDEN_CARROT,
                Material.COOKED_MUTTON,
                Material.COOKED_PORKCHOP,
                Material.COOKED_SALMON,
                Material.COOKED_BEEF,
                Material.BAKED_POTATO,
                Material.BEETROOT,
                Material.BEETROOT_SOUP,
                Material.BREAD,
                Material.CARROT,
                Material.COOKED_CHICKEN,
                Material.COOKED_COD,
                Material.COOKED_RABBIT,
                Material.MUSHROOM_STEW,
                Material.RABBIT_STEW,
                Material.SUSPICIOUS_STEW,
                Material.APPLE,
                Material.CHORUS_FRUIT,
                Material.DRIED_KELP,
                Material.MELON_SLICE,
                Material.POISONOUS_POTATO,
                Material.POTATO,
                Material.PUMPKIN_PIE,
                Material.BEEF,
                Material.CHICKEN,
                Material.MUTTON,
                Material.PORKCHOP,
                Material.RABBIT,
                Material.SWEET_BERRIES,
                Material.COOKIE,
                Material.GLOW_BERRIES,
                Material.HONEY_BOTTLE,
                Material.PUFFERFISH,
                Material.COD,
                Material.SALMON,
                Material.ROTTEN_FLESH,
                Material.SPIDER_EYE,
                Material.TROPICAL_FISH
        ));

        return res;
    }

    public int getFoodLevelOf(Material item) {
        int res = 0;
        if (this.getAllFoodOfTheGame().contains(item)){
            switch (item) {
                case BEETROOT:
                case DRIED_KELP:
                case POTATO:
                case PUFFERFISH:
                case TROPICAL_FISH:
                    res = 1;
                    break;
                case COOKIE:
                case GLOW_BERRIES:
                case MELON_SLICE:
                case POISONOUS_POTATO:
                case CHICKEN:
                case COD:
                case MUTTON:
                case SALMON:
                case SPIDER_EYE:
                case SWEET_BERRIES:
                    res = 2;
                    break;
                case CARROT:
                case BEEF:
                case PORKCHOP:
                case RABBIT:
                    res = 3;
                    break;
                case APPLE:
                case CHORUS_FRUIT:
                case ROTTEN_FLESH:
                    res = 4;
                    break;
                case BAKED_POTATO:
                case BREAD:
                case COOKED_COD:
                case COOKED_RABBIT:
                    res = 5;
                    break;
                case BEETROOT_SOUP:
                case COOKED_CHICKEN:
                case COOKED_MUTTON:
                case COOKED_SALMON:
                case HONEY_BOTTLE:
                case MUSHROOM_STEW:
                case SUSPICIOUS_STEW:
                    res = 6;
                    break;
                case COOKED_PORKCHOP:
                case PUMPKIN_PIE:
                case COOKED_BEEF:
                    res = 8;
                    break;
                case RABBIT_STEW:
                    res = 10;
                    break;
            }
        }
        return res;
    }
}
