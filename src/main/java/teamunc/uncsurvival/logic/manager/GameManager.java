package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.LocationManager;
import teamunc.uncsurvival.utils.scoreboards.InGameInfoScoreboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameManager extends AbstractManager {

    private boolean isGameRunning = false;

    private TeamsManager teamsManager;
    private LocationManager locationManager;
    private ItemsManager itemsManager;
    private ParticipantManager participantManager;
    private ScoreboardManager scoreboardManager;
    private TimeManager timeManager;
    private PhaseManager phaseManager;

    public GameManager(UNCSurvival plugin) {
        super(plugin);
        this.locationManager = new LocationManager(plugin);
        this.itemsManager = new ItemsManager(plugin);
        this.participantManager = new ParticipantManager(plugin);
        this.scoreboardManager = new ScoreboardManager(plugin);
        this.teamsManager = new TeamsManager(plugin);
        this.timeManager = new TimeManager(plugin);
        this.phaseManager = new PhaseManager(plugin);

    }

    public Set<Player> getPlayersInGame() {
        Set<Player> set = new HashSet<>();

        for (GamePlayer p : this.participantManager.getGamePlayers()) {
            set.add(p.getBukkitPlayer());
        }

        return set;
    }

    public boolean start(CommandSender sender) {

        // error if Game is already Running
        if (this.isGameRunning == true) {
            this.messageTchatManager.sendMessageToPlayer("Game has already started !",sender, ChatColor.RED);
            return false;
        }

        // error if playersInGame < 1
        if (this.participantManager.getGamePlayers().size() < 1) {
            this.messageTchatManager.sendMessageToPlayer("You need a minimum of 1 player in the game !", sender, ChatColor.RED);
            return false;
        }

        // start the timer
        this.getTimeManager().startTimer();

        // Register all players for thirst
        ThirstActualiser.getInstance().registerPlayers(new ArrayList<>(this.participantManager.getBukkitPlayers()));

        // InGameInfoScoreboard
        addInGameScoreboard();

        return true;
    }

    /**
     *
     * @param player
     * @return false if the player already exist in the list
     */
    public boolean addPlayerToTheGame(Player player) {

        // adding and creating GamePlayer
        this.participantManager.addPlayer(player);

        return true;
    }

    public void addInGameScoreboard() {
        for (GamePlayer p : this.participantManager.getGamePlayers()) {
            Player player = p.getBukkitPlayer();
            this.scoreboardManager.addScoreboard(new InGameInfoScoreboard(player));
        }
    }

    /**
     *
     * @param player
     * @return false if the player aren't in the list
     */
    public boolean removePlayerToTheGame(Player player) {
        this.participantManager.removePlayer(player);
        return true;
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }
    public ItemsManager getItemsManager() {
        return this.itemsManager;
    }
    public TeamsManager getTeamsManager() {
        return this.teamsManager;
    }
    public ParticipantManager getParticipantManager() {
        return participantManager;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public PhaseManager getPhaseManager() {
        return phaseManager;
    }
    public TimeManager getTimeManager() {
        return timeManager;
    }
}
