package teamunc.uncsurvival.logic.manager;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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
import java.util.List;

public class GameManager extends AbstractManager {

    /* Configuration */
    private GameConfiguration gameConfiguration;
    private GameStats gameStats;

    /* Mananger */
    private TeamsManager teamsManager;
    private CustomBlockManager customBlockManager;
    private ItemsManager itemsManager;
    private ParticipantManager participantManager;
    private ScoreboardManager scoreboardManager;
    private TimeManager timeManager;
    private InterfacesManager interfacesManager;
    private GameEventsManager gameEventsManager;
    private CombatDisconnectManager combatDisconnectManager;

    private CountdownPhaseTask timerTask;
    private AdvancementManager advancementManager;

    public GameManager(UNCSurvival plugin) {
        super(plugin);

        this.loadGameConfiguration();
        this.loadGameStats();

        this.itemsManager = new ItemsManager(plugin,gameConfiguration,this.gameStats);
        this.participantManager = new ParticipantManager(plugin);
        this.scoreboardManager = new ScoreboardManager(plugin);
        this.timeManager = new TimeManager(plugin);
        this.teamsManager = new TeamsManager(plugin);
        this.interfacesManager = new InterfacesManager(plugin);
        this.customBlockManager = new CustomBlockManager(plugin);
        this.gameEventsManager = new GameEventsManager(plugin);
        this.advancementManager = new AdvancementManager(plugin);
        this.combatDisconnectManager = new CombatDisconnectManager(plugin);

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

    public void initStarting() {
        this.timerTask = new CountdownPhaseTask(15, 0, 0, 0);
        timerTask.runTaskTimer(this.plugin,0, 20);
        this.getGameStats().setCurrentPhase(PhaseEnum.LANCEMENT);
    }

    public void appliqueStartConstraints(Player bukkitPlayer) {
        GamePlayer player = this.getParticipantManager().getGamePlayer(bukkitPlayer.getName());
        Team team = this.getParticipantManager().getTeamForPlayer(bukkitPlayer.getName());
        if (player != null && team != null && !player.isStartRegistered()) {
            player.setStartRegistered(true);

            bukkitPlayer.teleport(team.getSpawnPoint());
            bukkitPlayer.setBedSpawnLocation(team.getSpawnPoint(),true);

            bukkitPlayer.setGameMode(GameMode.SURVIVAL);
            bukkitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14);
            bukkitPlayer.getInventory().clear();

            this.scoreboardManager.addScoreboard(new InGameInfoScoreboard(bukkitPlayer));
            ThirstActualiser.getInstance().registerPlayers(new ArrayList<>(List.of(player)));
        }
    }

    public boolean start() {

        // clear advancements
        this.getAdvancementManager().clearAll();

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

        for (GamePlayer gp :
                this.getParticipantManager().getGamePlayers()) {
            gp.setStartRegistered(true);
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
                break;
        }

        this.getItemsManager().registerGoalItems(gameStats.getCurrentPhase());
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
            if(gamePlayer.isOnline()) {
                Player p = gamePlayer.getBukkitPlayer();
                p.setGameMode(GameMode.SURVIVAL);
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(14);
                p.getInventory().clear();
            }
        });

        // Gamerules
        Bukkit.getWorlds().get(0).setDifficulty(Difficulty.HARD);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.MOB_GRIEFING,false);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_DAYLIGHT_CYCLE,true);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.NATURAL_REGENERATION,false);
    }

    public void addInGameScoreboard() {
        for (GamePlayer p : this.participantManager.getGamePlayers()) {
            Player player = p.getBukkitPlayer();
            if(player != null)
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
    public GameEventsManager getGameEventsManager() {
        return gameEventsManager;
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
    public CombatDisconnectManager getCombatDisconnectManager() { return combatDisconnectManager; }

    public void save() {
        this.getTeamsManager().saveTeams();
        this.getCustomBlockManager().saveCustomBlock();
        this.getAdvancementManager().save();
    }

    public AdvancementManager getAdvancementManager() {
        return this.advancementManager;
    }
}
