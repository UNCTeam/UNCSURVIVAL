package teamunc.uncsurvival.logic.manager;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.scoreboards.InGameInfoScoreboard;

import java.util.*;

public class GameManager extends AbstractManager {

    /* Configuration */
    private GameConfiguration gameConfiguration;
    private GameRuleConfiguration gameRuleConfiguration;
    private GameStats gameStats;

    /* Mananger */
    private TeamsManager teamsManager;
    private ItemsManager itemsManager;
    private ParticipantManager participantManager;
    private ScoreboardManager scoreboardManager;
    private TimeManager timeManager;
    private PhaseManager phaseManager;
    private InterfacesManager interfacesManager;

    public GameManager(UNCSurvival plugin) {
        super(plugin);

        this.loadGameRuleConfiguration();
        this.loadGameConfiguration();
        this.loadGameStats();

        this.itemsManager = new ItemsManager(plugin,gameConfiguration);
        this.participantManager = new ParticipantManager(plugin);
        this.scoreboardManager = new ScoreboardManager(plugin);
        this.timeManager = new TimeManager(plugin);
        this.phaseManager = new PhaseManager(plugin);
        this.teamsManager = new TeamsManager(plugin);
        this.interfacesManager = new InterfacesManager(plugin);

        this.afterReload();
    }

    public void afterReload() {
        if (!gameStats.isGameStarted()) return;

        this.timeManager.startTimer();
    }

    public void loadGameRuleConfiguration() {
        this.gameRuleConfiguration = this.plugin.getFileManager().loadGameRuleConfiguration();
    }

    public void loadGameConfiguration() {
        this.gameConfiguration = this.plugin.getFileManager().loadGameConfiguration();
    }
    public void loadGameStats() {
        this.gameStats = this.plugin.getFileManager().loadGameStats();
    }

    public World getMainWorld() {
        return Bukkit.getWorlds().get(0);
    }

    public boolean start(CommandSender sender) {

        // error if Game is already Running
        if (this.gameStats.isGameStarted()) {
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

        // teleport everyplayers at their spawnpoint
        this.getTeamsManager().teleportEveryPlayers();

        // set survival conditions
        this.setSurvivalConditions();

        // Register all players for thirst
        ThirstActualiser.getInstance().registerPlayers(new ArrayList<>(this.participantManager.getGamePlayers()));

        // InGameInfoScoreboard
        addInGameScoreboard();
        this.scoreboardManager.initStatsScoreboard();

        // save game started info
        this.gameStats.setGameStarted(true);
        return true;
    }

    private void setSurvivalConditions() {
        // All spec
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.setGameMode(GameMode.SPECTATOR);
        });

        // set Attributes
        this.participantManager.getGamePlayers().forEach(gamePlayer -> {
            if(gamePlayer.isOneline()) {
                Player p = gamePlayer.getBukkitPlayer();
                p.setGameMode(GameMode.SURVIVAL);
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14);
                p.getInventory().clear();
            }
        });

        // Gamerules
        Bukkit.getWorlds().get(0).setGameRule(GameRule.NATURAL_REGENERATION,false);
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
    public InterfacesManager getInterfacesManager() {
        return this.interfacesManager;
    }
    public GameConfiguration getGameConfiguration() {
        return gameConfiguration;
    }

    public void save() {
        this.getTeamsManager().saveTeams();
    }
}
