package teamunc.uncsurvival.logic.manager;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.tasks.CountdownPhaseTask;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.utils.scoreboards.InGameInfoScoreboard;

import java.util.ArrayList;

public class GameManager extends AbstractManager {

    /* Configuration */
    private GameConfiguration gameConfiguration;
    private GameRuleConfiguration gameRuleConfiguration;
    private GameStats gameStats;

    /* Mananger */
    private TeamsManager teamsManager;
    private CustomBlockManager customBlockManager;
    private ItemsManager itemsManager;
    private ParticipantManager participantManager;
    private ScoreboardManager scoreboardManager;
    private TimeManager timeManager;
    private InterfacesManager interfacesManager;

    private CountdownPhaseTask timerTask;

    public GameManager(UNCSurvival plugin) {
        super(plugin);

        this.loadGameRuleConfiguration();
        this.loadGameConfiguration();
        this.loadGameStats();

        this.itemsManager = new ItemsManager(plugin,gameConfiguration);
        this.participantManager = new ParticipantManager(plugin);
        this.scoreboardManager = new ScoreboardManager(plugin);
        this.timeManager = new TimeManager(plugin);
        this.teamsManager = new TeamsManager(plugin);
        this.interfacesManager = new InterfacesManager(plugin);
        this.customBlockManager = new CustomBlockManager(plugin);

        this.afterReload();
    }

    public void loadTimer() {
        switch (this.gameStats.getCurrentPhase()) {
            case PHASE1:
                this.timerTask = new CountdownPhaseTask(gameConfiguration.getDatePhase2());
                timerTask.runTaskTimer(this.plugin,0, 20);
                break;
            case PHASE2:
                this.timerTask = new CountdownPhaseTask(gameConfiguration.getDatePhase3());
                timerTask.runTaskTimer(this.plugin,0, 20);
                break;
            case PHASE3:
                this.timerTask = new CountdownPhaseTask(gameConfiguration.getDateFin());
                timerTask.runTaskTimer(this.plugin,0, 20);
                break;
        }
    }

    public void afterReload() {
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

    public CountdownPhaseTask getTimerTask() { return timerTask; }

    public GameStats getGameStats() { return gameStats; }

    public GameRuleConfiguration getGameRuleConfiguration() { return gameRuleConfiguration; }

    public void initStarting() {
        this.timerTask = new CountdownPhaseTask(15, 0, 0, 0);
        timerTask.runTaskTimer(this.plugin,0, 20);
        this.getGameStats().setCurrentPhase(PhaseEnum.LANCEMENT);
    }

    public boolean start() {

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
        this.gameStats.setCurrentPhase(PhaseEnum.PHASE1);
        this.startPhase1();

        // reset team score
        for (Team t : this.teamsManager.getAllTeams()) {
            t.resetScore();
        }

        return true;
    }

    public void goNextPhase() {
        switch (gameStats.getCurrentPhase()) {
            case LANCEMENT:
                this.timerTask.cancel();
                plugin.getGameManager().start();
                break;
            case PHASE1:
                this.timerTask.cancel();
                plugin.getGameManager().startPhase2();
                break;
            case PHASE2:
                this.timerTask.cancel();
                plugin.getGameManager().startPhase3();
                break;
            case PHASE3:
                this.timerTask.cancel();
                plugin.getGameManager().startEnding();
                break; }
    }

    /**
     * Lancement PHASE 1
     */
    public void startPhase1() {
        this.getGameStats().setCurrentPhase(PhaseEnum.PHASE1);
        this.timerTask = new CountdownPhaseTask(this.gameConfiguration.getDatePhase2());
        timerTask.runTaskTimer(this.plugin,0, 20);
    }

    /**
     * Lancement PHASE 2
     */
    public void startPhase2() {
        this.getGameStats().setCurrentPhase(PhaseEnum.PHASE2);
        this.timerTask = new CountdownPhaseTask(this.gameConfiguration.getDatePhase3());
        timerTask.runTaskTimer(UNCSurvival.getInstance(),0, 20);
        this.plugin.getGameManager().getItemsManager().replaceCraftPhase2();
    }

    /**
     * Lancement PHASE 3
     */
    public void startPhase3() {
        this.getGameStats().setCurrentPhase(PhaseEnum.PHASE3);
        this.timerTask = new CountdownPhaseTask(this.gameConfiguration.getDateFin());
        timerTask.runTaskTimer(UNCSurvival.getInstance(),0, 20);
        this.plugin.getGameManager().getItemsManager().replaceCraftPhase3();
    }

    public void startEnding() {
        this.getGameStats().setCurrentPhase(PhaseEnum.FIN);
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
            this.scoreboardManager.
                    addScoreboard(new InGameInfoScoreboard(player));
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
    public CustomBlockManager getCustomBlockManager() { return customBlockManager; }
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
