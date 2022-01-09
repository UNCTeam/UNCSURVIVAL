package teamunc.uncsurvival.logic.manager;

import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParticipantManager extends AbstractManager{

    private ArrayList<GamePlayer> gamePlayers = new ArrayList<>();

    public ParticipantManager(UNCSurvival plugin) {
        super(plugin);
    }


    public GamePlayer addPlayer(Player bukkitPlayer) {
        GamePlayer gamePlayer = new GamePlayer(bukkitPlayer);
        this.gamePlayers.add(gamePlayer);
        return gamePlayer;
    }

    public GamePlayer removePlayer(Player bukkitPlayer) {
        GamePlayer res = null;
        for (GamePlayer gp : gamePlayers) {
            if (gp.getBukkitPlayer().getUniqueId() == bukkitPlayer.getUniqueId()) {
                this.gamePlayers.remove(gp);
                res = gp;
            }
        }

        return res;
    }

    public ArrayList<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public Set<Player> getBukkitPlayers() {
        Set<Player> players = new HashSet<>();
        for (GamePlayer gp : gamePlayers) {
            players.add(gp.getBukkitPlayer());
        }

        return players;
    }
}
