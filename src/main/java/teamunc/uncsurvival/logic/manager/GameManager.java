package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameManager extends AbstractManager {

    private Set<Player> playersInGame = new HashSet<>();
    private boolean isGameRunning = false;

    private TeamsManager teamsManager;

    public GameManager(UNCSurvival plugin) {
        super(plugin);
        this.teamsManager = new TeamsManager(plugin);
    }

    public Set<Player> getPlayersInGame() {
        return playersInGame;
    }

    public boolean start(CommandSender sender) {

        // error if Game is already Running
        if (this.isGameRunning == true) {
            this.messageTchatManager.sendMessageToPlayer("Game has already started !",sender, ChatColor.RED);
            return false;
        }

        // error if playersInGame < 1
        if (this.playersInGame.size() < 1) {
            this.messageTchatManager.sendMessageToPlayer("You need a minimum of 1 player in the game !", sender, ChatColor.RED);
            return false;
        }

        // start the timer
        UNCSurvival.getInstance().getTimeManager().startTimer();

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
