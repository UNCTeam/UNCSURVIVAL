package teamunc.uncsurvival.logic.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class GamePlayer implements Serializable {
    private final UUID uuid;
    private static int MAXFOODBUFFER = 8;
    private int waterLevel;
    /**
     * si timeBeforeCovidExpansion == -1 alors pas de covid, sinon c'est le temps avant le prochain covid (en seconde)
     * décrémenter dans timeManager#eachSecondes
     */
    private int timeBeforeCovidExpansion = -1;

    private ArrayList<Material> queueOfEatenFood;
    private boolean inDuel;
    private int duelsWon;

    public GamePlayer(Player bukkitPlayer) {
        this.uuid = bukkitPlayer.getUniqueId();
        this.queueOfEatenFood = new ArrayList<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Boolean isOnline() {
        return getOfflinePlayer().isOnline();
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(this.uuid);
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
    }

    public boolean isCovided() {
        return (this.timeBeforeCovidExpansion >= 0);
    }

    public void ActiveCovided() {
        this.timeBeforeCovidExpansion = 4;
    }

    public ChatColor getTeamColor() {
        return UNCSurvival.getInstance().getGameManager().getParticipantManager().getTeamForPlayer(this.getBukkitPlayer()).getChatColor();
    }

    /**
     * boucle de 4 à 0
     * @return true si le temps est à 0 apres la seconde passer
     */
    public boolean passerUneSecondeCovid() {
        if (this.timeBeforeCovidExpansion - 1 == -1) this.timeBeforeCovidExpansion = 4;
        this.timeBeforeCovidExpansion -= 1;
        return this.timeBeforeCovidExpansion == 0;
    }

    public void cureCovid() {
        this.timeBeforeCovidExpansion = -1;
    }

    public int EatHowOften(Material food) {
        return (int) this.queueOfEatenFood.stream().filter(material -> material == food).count();
    }

    public void addToEatenFoodQueue(Material food) {
        this.queueOfEatenFood.add(0,food);
        if (this.queueOfEatenFood.size() > MAXFOODBUFFER) this.queueOfEatenFood.remove(8);
    }

    public ArrayList<Material> getQueueOfEatenFood() {
        return queueOfEatenFood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePlayer that = (GamePlayer) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    public void setInDuel(boolean b) {
        this.inDuel = b;
    }

    public boolean isInDuel() {
        return inDuel;
    }

    public int getDuelsWon() {
        return this.duelsWon;
    }

    public void justWinADuel() {
        this.duelsWon += 1;
    }
}
