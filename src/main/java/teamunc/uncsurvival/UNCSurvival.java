package teamunc.uncsurvival;

import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.commandesListeners.*;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.utils.LocationManager;
import teamunc.uncsurvival.utils.scoreboards.InfoScoreboardSideBarManager;

public class UNCSurvival extends JavaPlugin {

    private GameManager gameManager;
    private LocationManager locationManager;

    private static UNCSurvival instance;

    @Override
    public void onEnable() {
        instance = this;

        this.gameManager = new GameManager(this);
        this.locationManager = new LocationManager(this);

        // register commands
        this.getCommand("startuncsurvival").setExecutor(new StartCmdExec(this));

        this.getCommand("addplayer").setExecutor(new AddPlayerCmdExec(this));
        this.getCommand("removeplayer").setExecutor(new RemovePlayerCmdExec(this));
        this.getCommand("getplayersingame").setExecutor(new GetPlayersInGameCmdExec(this));

        this.getCommand("addspawnpoint").setExecutor(new AddSpawnPointCmdExec(this));
        this.getCommand("removespawnpoint").setExecutor(new RemoveSpawnPointCmdExec(this));

        // scoreboard init
        InfoScoreboardSideBarManager.getInstance();

    }

    public static UNCSurvival getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public LocationManager getLocationManager() {
        return this.locationManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
