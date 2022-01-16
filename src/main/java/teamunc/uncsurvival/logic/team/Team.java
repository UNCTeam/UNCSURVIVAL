package teamunc.uncsurvival.logic.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.Region;

import java.io.Serializable;
import java.util.*;

public class Team implements Serializable {

    private final UUID uuid;
    private String name;
    private ChatColor chatColor;
    private Map<Material, Integer> itemsProduction = new HashMap<>();
    private int bonusScore = 0;
    private Location spawnPoint;
    private int range = 10;
    private Region region;
    /**
     * l'index de la list correspond au numero de l'item Goal (1 à 5)
     */
    private ArrayList<Location> interfaces = new ArrayList<>();
    private final List<GamePlayer> members;

    public Team(String name, ChatColor chatColor, Location spawnPoint) {
        this.name = name;
        this.chatColor = chatColor;
        this.members = new ArrayList<>();

        this.uuid = UUID.randomUUID();

        // todo calculs de la location

        this.spawnPoint = spawnPoint;
        this.region = new Region(spawnPoint, range);
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

    public int getItemsProduction(Material item) {
        return itemsProduction.get(item);
    }

    public void addItemsProduction(Material item, int number) {
        this.itemsProduction.put(item,this.itemsProduction.get(item) + number);
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
        for (Material i: this.itemsProduction.keySet()) {
            total = UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemScore(i) * this.itemsProduction.get(i);
        }
        return total;
    }

    public void addABonusScore(int score) {
        this.bonusScore += score;
    }
}
