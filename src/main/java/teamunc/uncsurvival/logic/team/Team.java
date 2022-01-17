package teamunc.uncsurvival.logic.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.GoalCustomInterface;
import teamunc.uncsurvival.logic.interfaces.TeamCustomInterface;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.Region;

import java.io.Serializable;
import java.util.*;

public class Team implements Serializable {

    private final UUID uuid;
    private String name;
    private ChatColor chatColor;
    private ArrayList<Integer> itemsProduction = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    private int bonusScore = 0;
    private Location spawnPoint;
    private ArrayList<Location> interfaceLocations = new ArrayList<>();
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
        // calculs des locations interface goal
        this.interfaceLocations = new ArrayList<>(
                Arrays.asList(
                    new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()),
                    new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()+8),
                    new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6),
                    new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6),
                        new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-8,spawnPoint.getBlockY(),spawnPoint.getBlockZ())
            )
        );
        // placement des block
        for (int i = 0; i < this.interfaceLocations.size(); i++) {
            this.interfaceLocations.get(i).getBlock().setType(Material.BARRIER);
        }

        // adding custom block style
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);

        for (int i = 0; i < this.interfaceLocations.size(); i++) {
            Location loc = this.interfaceLocations.get(i).clone().add(0.5,0,0.5);
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.addScoreboardTag("INTERFACE_"+this.name+"_"+i);
            armorStand.getEquipment().setHelmet(texture);
        }

        // interface team
        this.interfaceTeam = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()-8);
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
        Location oldLoc = this.interfaceLocations.get(itemNumber);
        ArmorStand oldArmorStand = (ArmorStand) oldLoc.getWorld().getNearbyEntities(oldLoc,3,3,3,entity -> entity.getScoreboardTags().contains("INTERFACE_"+this.name+"_"+itemNumber));
        if (oldArmorStand != null) oldArmorStand.remove();
        oldLoc.getBlock().setType(Material.AIR);

        // nouvelle pos
        this.interfaceLocations.set(itemNumber,newLocation);
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfaceLocations.get(itemNumber),new GoalCustomInterface(itemNumber,this));

        newLocation.getBlock().setType(Material.BARRIER);

        Location loc = newLocation.clone().add(0.5,0,0.5);

        // adding custom block style
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.addScoreboardTag("INTERFACE_"+this.name+"_"+itemNumber);
        armorStand.getEquipment().setHelmet(texture);
    }

    public void ConsumeAllGoalItems() {
        // TODO loc11.getBlock().getBlockData()

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
        int total = 0;
        for (int i: this.itemsProduction) {
            total = UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemPrice(i) * this.itemsProduction.get(i);
        }
        return total;
    }

    public void addABonusScore(int score) {
        this.bonusScore += score;
    }

    public void registerInterfaces() {
        for (int i = 0; i < this.interfaceLocations.size(); i++) {
            UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfaceLocations.get(i),new GoalCustomInterface(i,this));
        }
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfaceTeam,new TeamCustomInterface(this));
    }
}
