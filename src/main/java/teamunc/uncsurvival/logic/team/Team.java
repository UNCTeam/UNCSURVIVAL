package teamunc.uncsurvival.logic.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.CustomBlock;
import teamunc.uncsurvival.logic.interfaces.GoalCustomInterface;
import teamunc.uncsurvival.logic.interfaces.TeamCustomInterface;
import teamunc.uncsurvival.logic.manager.ItemsManager;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.Region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Team implements Serializable {

    private final UUID uuid;
    private String name;
    private ChatColor chatColor;
    private ArrayList<Integer> itemsProduction = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    private int bonusScore = 0;
    private Location spawnPoint;
    private ArrayList<CustomBlock> interfacesGoals = new ArrayList<>();
    private Location interfaceTeam;
    private int range = 10;
    private Region region;

    private final List<GamePlayer> members;

    public Team(String name, ChatColor chatColor, Location spawnPoint) {
        this.name = name;
        this.chatColor = chatColor;
        this.members = new ArrayList<>();

        this.uuid = UUID.randomUUID();

        this.spawnPoint = spawnPoint;
        this.region = new Region(spawnPoint, range);

        this.initInterfaceBlockAtStart();
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void initInterfaceBlockAtStart() {
        // block texture
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);

        // calculs des locations interface goal
        ArrayList<Location> locationTemp = new ArrayList<>();

        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()+8));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()));

        // placement des block
        for (int i = 0; i < locationTemp.size(); i++) {
            locationTemp.get(i).getBlock().setType(Material.BARRIER);
        }

        // creating/assembling custom block
        for (int i = 0; i < locationTemp.size(); i++) {
            Location loc = locationTemp.get(i).clone().add(0.5,0,0.5);
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.addScoreboardTag("INTERFACE_"+this.name+"_"+i);
            armorStand.getEquipment().setHelmet(texture);

            this.interfacesGoals.add(new CustomBlock(locationTemp.get(i),armorStand));
        }

        // interface team
        this.interfaceTeam = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()-8);
        this.interfaceTeam.getBlock().setType(Material.BARRIER);

        Location loc = this.interfaceTeam.clone().add(0.5,0,0.5);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.addScoreboardTag("MAIN_INTERFACE_"+this.name);
        armorStand.getEquipment().setHelmet(texture);

        // register for the first time
        this.registerInterfaces();
    }

    public void moveInterfaceGoal(int itemNumber,Location newLocation) {
        // supression last pos
        CustomBlock cBlock = this.interfacesGoals.get(itemNumber);
        Location oldLoc = cBlock.getLocation();
        ArmorStand oldArmorStand = cBlock.getArmorStand();
        if (oldArmorStand != null) oldArmorStand.remove();
        oldLoc.getBlock().setType(Material.AIR);
        oldLoc.getWorld().setChunkForceLoaded(oldLoc.getChunk().getX(), oldLoc.getChunk().getZ(), false);

        // nouvelle pos
        newLocation.getBlock().setType(Material.BARRIER);

        Location loc = newLocation.clone().add(0.5,0,0.5);
        loc.getWorld().setChunkForceLoaded(loc.getChunk().getX(),loc.getChunk().getZ(),true);
        // adding custom block style
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.getEquipment().setHelmet(texture);

        this.interfacesGoals.set(itemNumber,new CustomBlock(newLocation,armorStand));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfacesGoals.get(itemNumber).getLocation(),new GoalCustomInterface(itemNumber,this));
    }

    public void ConsumeAllGoalItems() {
        final ItemsManager itemsManager = UNCSurvival.getInstance().getGameManager().getItemsManager();
        ArrayList<CustomBlock> goals = this.interfacesGoals;
        for (int i = 0, goalsSize = goals.size(); i < goalsSize; i++) {
            CustomBlock cBlock = goals.get(i);
            ArrayList<Block> blocksToCheck = new ArrayList<>(Arrays.asList(
                    cBlock.getLocation().clone().add(1, 0, 0).getBlock(),
                    cBlock.getLocation().clone().add(0, 0, 1).getBlock(),
                    cBlock.getLocation().clone().add(-1, 0, 0).getBlock(),
                    cBlock.getLocation().clone().add(0, 0, -1).getBlock()
            ));

            for (Block b : blocksToCheck) {
                if ( b.getType() == Material.CHEST ) {
                    Chest block = (Chest) b.getState();
                    for (ItemStack itemStack : block.getBlockInventory().getContents()) {
                        if (itemStack != null && itemStack.getType() == itemsManager.getItem(i)) {
                            this.itemsProduction.set(i,this.itemsProduction.get(i) + itemStack.getAmount());
                            block.getBlockInventory().remove(itemStack);
                        }
                    }
                }
            }
        }
    }

    public boolean hasMember(GamePlayer gamePlayer) {
        for (GamePlayer gamePlayer1 : this.members) {
            if(gamePlayer1.getUUID().equals(gamePlayer.getUUID())) { return true; }
        }
        return false;
    }

    public void quit(GamePlayer gamePlayer) {
        if(hasMember(gamePlayer)) {
            this.members.remove(gamePlayer);
        }
    }

    public void join(GamePlayer gamePlayer) {
        if(!hasMember(gamePlayer)) {
            this.members.add(gamePlayer);
        }
    }

    public int getItemsProduction(int itemNumber) {
        return itemsProduction.get(itemNumber);
    }

    public void addItemsProduction(int itemNumber, int number) {
        this.itemsProduction.set(itemNumber,this.itemsProduction.get(itemNumber) + number);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public List<GamePlayer> getMembers() {
        return members;
    }

    public int getScore() {
        int total = this.bonusScore;
        for (int j = 0; j < this.itemsProduction.size(); j++) {
            total += UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemPrice(j) * this.itemsProduction.get(j);
        }
        return total;
    }

    public void addABonusScore(int score) {
        this.bonusScore += score;
    }

    public void registerInterfaces() {
        for (int i = 0; i < this.interfacesGoals.size(); i++) {
            UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfacesGoals.get(i).getLocation(),new GoalCustomInterface(i,this));
        }
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfaceTeam,new TeamCustomInterface(this));
    }

    public void postLoad() {
        registerInterfaces();
        this.interfacesGoals.forEach(customBlock -> {
            Location loc = customBlock.getLocation().clone().add(0.5,0,0.5);

            // adding custom block style
            ItemStack texture = new ItemStack(Material.DROPPER, 1);
            ItemMeta textMeta = texture.getItemMeta();
            textMeta.setCustomModelData(2);
            texture.setItemMeta(textMeta);
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.getEquipment().setHelmet(texture);

            customBlock.setArmorStand(armorStand);
        });
    }

    public void onDisable() {
        this.interfacesGoals.forEach(customBlock -> {
            customBlock.getArmorStand().remove();
            customBlock.setArmorStand(null);
        });
    }

    public Region getRegion() {
        return region;
    }

    public void resetScore() {
        this.itemsProduction = new ArrayList<>(Arrays.asList(0,0,0,0,0));
        this.bonusScore = 0;
    }
}
