package teamunc.uncsurvival.logic.player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.utils.scoreboards.FastBoard;

import java.util.UUID;

public class GamePlayer {
    protected final UUID uuid;
    protected final FastBoard scoreboard;
    protected final Player bukkitPlayer;

    public GamePlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        this.uuid = this.bukkitPlayer.getUniqueId();
        scoreboard = new FastBoard(bukkitPlayer);
    }

    public FastBoard getScoreboard() {
        return scoreboard;
    }

    public void initScoreboard() {
        this.scoreboard.updateTitle(ChatColor.BOLD + "" + ChatColor.GOLD + "UNC Survival " + ChatColor.GOLD + "-" + ChatColor.AQUA + " Saison 1");
        this.scoreboard.updateLine(1,"TEST1");
        this.scoreboard.updateLine(2,"TEST2");
        this.scoreboard.updateLine(3,"TEST3");
        this.scoreboard.updateLine(4,"TEST4");
    }

    public UUID getUUID() {
        return uuid;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }
}
