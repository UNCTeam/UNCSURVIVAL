package teamunc.uncsurvival.logic.manager;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.utils.LocationManager;
import teamunc.uncsurvival.utils.MessageTchatManager;
import teamunc.uncsurvival.utils.timer.TimeManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameManager {

    private final UNCSurvival plugin;

    private Set<Player> playersInGame = new HashSet<>();
    private boolean isGameRunning = false;

    public GameManager(UNCSurvival plugin) {
        this.plugin = plugin;
    }

    public Set<Player> getPlayersInGame() {
        return playersInGame;
    }

    public boolean start(CommandSender sender) {

        // error if Game is already Running
        if (this.isGameRunning == true) {
            MessageTchatManager.getInstance().sendMessageToPlayer("Game has already started !",sender, ChatColor.RED);
            return false;
        }

        // error if playersInGame < 1
        if (this.playersInGame.size() < 1) {
            MessageTchatManager.getInstance().sendMessageToPlayer("You need a minimum of 1 player in the game !", sender, ChatColor.RED);
            return false;
        }

        // test si liste joueurs < locations
        if (this.playersInGame.size() > this.plugin.getLocationManager().getSpawnPoints().size()) {
            MessageTchatManager.getInstance().sendMessageToPlayer("SpawnPoints registred are lower than player in game ! ", sender, ChatColor.RED);
            return false;
        }

        // tp all players
        plugin.getLocationManager().spreadPlayerWithSpawnPointList(this.playersInGame);

        // start the timer
        TimeManager.getInstance().startTimer();

        // Register all players for thirst
        ThirstActualiser.getInstance().registerPlayers(new ArrayList<>(this.playersInGame));

        return true;
    }

    /**
     *
     * @param player
     * @return false if the player already exist in the list
     */
    public boolean addPlayerToTheGame(Player player) {
        return this.playersInGame.add(player);
    }

    /**
     *
     * @param player
     * @return false if the player aren't in the list
     */
    public boolean removePlayerToTheGame(Player player) {
        return this.playersInGame.remove(player);
    }
}