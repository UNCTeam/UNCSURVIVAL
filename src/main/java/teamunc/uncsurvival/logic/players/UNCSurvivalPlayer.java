package teamunc.uncsurvival.logic.players;

import org.bukkit.entity.Player;

public class UNCSurvivalPlayer {
    private Player player;

    public Player getBukkitPlayer() {
        return player;
    }

    public UNCSurvivalPlayer(Player player) {
        this.player = player;
    }
}
