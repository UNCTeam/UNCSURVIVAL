package teamunc.uncsurvival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.commandesListeners.*;
import teamunc.uncsurvival.utils.MessageTchatManager;
import teamunc.uncsurvival.utils.scoreboards.InfoScoreboardSideBarManager;

public final class UNCSURVIVAL extends JavaPlugin {

    private static JavaPlugin plugin;
    public static JavaPlugin getPlugin() {
        return UNCSURVIVAL.plugin;
    }

    @Override
    public void onEnable() {
        UNCSURVIVAL.plugin = this;
        // register commands
        this.getCommand("startuncsurvival").setExecutor(new StartCmdExec());

        this.getCommand("addplayer").setExecutor(new AddPlayerCmdExec());
        this.getCommand("removeplayer").setExecutor(new RemovePlayerCmdExec());
        this.getCommand("getplayersingame").setExecutor(new GetPlayersInGameCmdExec());

        this.getCommand("addspawnpoint").setExecutor(new AddSpawnPointCmdExec());
        this.getCommand("removespawnpoint").setExecutor(new RemoveSpawnPointCmdExec());

        // scoreboard init
        InfoScoreboardSideBarManager.getInstance();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
