package teamunc.uncsurvival.logic.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.utils.scoreboards.FastBoard;

import java.util.UUID;

public class GamePlayer {
    protected final UUID uuid;
    protected final Player bukkitPlayer;

    public GamePlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.uuid = this.bukkitPlayer.getUniqueId();
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }
}
