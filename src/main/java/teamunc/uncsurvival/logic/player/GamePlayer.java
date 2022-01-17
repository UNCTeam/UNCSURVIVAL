package teamunc.uncsurvival.logic.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.utils.scoreboards.FastBoard;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class GamePlayer implements Serializable {
    private final UUID uuid;
    private int waterLevel;

    public GamePlayer(Player bukkitPlayer) {
        this.uuid = bukkitPlayer.getUniqueId();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Boolean isOneline() {
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
}
