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

        this.initInterfaceBlock();
        this.armorStandsCustomBlock();
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void armorStandsCustomBlock() {
        ArrayList<ArmorStand> customblockStyles = new ArrayList<>();
        Location loc1 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()).add(0.5,0,0.5);
        Location loc2 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()).add(0.5,0,0.5);
        Location loc3 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()+8).add(0.5,0,0.5);
        Location loc4 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6).add(0.5,0,0.5);
        Location loc5 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6).add(0.5,0,0.5);
        Location locTeam = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()-8).add(0.5,0,0.5);

        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(loc1, EntityType.ARMOR_STAND));
        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(loc2, EntityType.ARMOR_STAND));
        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(loc3, EntityType.ARMOR_STAND));
        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(loc4, EntityType.ARMOR_STAND));
        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(loc5, EntityType.ARMOR_STAND));
        customblockStyles.add((ArmorStand) spawnPoint.getWorld().spawnEntity(locTeam, EntityType.ARMOR_STAND));

        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);

        customblockStyles.forEach(armorStand -> {
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.getEquipment().setHelmet(texture);
        });
    }

    public void initInterfaceBlock() {
        // calculs des locations interface goal et de l'interface de team (amelioration rayon)
        Location loc1 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+8,spawnPoint.getBlockY(),spawnPoint.getBlockZ());
        Location loc2 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-8,spawnPoint.getBlockY(),spawnPoint.getBlockZ());
        Location loc3 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()+8);
        Location loc4 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6);
        Location loc5 = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6);
        Location locTeam = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()-8);

        // placement des block
        loc1.getBlock().setType(Material.BARRIER);
        loc2.getBlock().setType(Material.BARRIER);
        loc3.getBlock().setType(Material.BARRIER);
        loc4.getBlock().setType(Material.BARRIER);
        loc5.getBlock().setType(Material.BARRIER);

        locTeam.getBlock().setType(Material.BARRIER);

        // adding
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(loc1,new GoalCustomInterface(1,this));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(loc2,new GoalCustomInterface(2,this));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(loc3,new GoalCustomInterface(3,this));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(loc4,new GoalCustomInterface(4,this));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(loc5,new GoalCustomInterface(5,this));

        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(locTeam,new TeamCustomInterface());
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
}
