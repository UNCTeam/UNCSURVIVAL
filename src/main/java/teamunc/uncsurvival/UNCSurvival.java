package teamunc.uncsurvival;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.commandesListeners.*;
import teamunc.uncsurvival.logic.manager.*;
import teamunc.uncsurvival.eventsListeners.AppleListener;

import java.io.FileReader;
import java.io.FileWriter;

public class UNCSurvival extends JavaPlugin {

    private GameManager gameManager;
    private MessageTchatManager messageTchatManager;

    private static UNCSurvival instance;


    @Override
    public void onEnable() {
        instance = this;

        Bukkit.broadcastMessage("UNCSurvival ON");

        // init Managers
        this.messageTchatManager = new MessageTchatManager(this);

        this.gameManager = new GameManager(this);

        // register commands
        this.getCommand("startuncsurvival").setExecutor(new StartCmdExec(this));
        this.getCommand("addplayer").setExecutor(new AddPlayerCmdExec(this));
        this.getCommand("removeplayer").setExecutor(new RemovePlayerCmdExec(this));
        this.getCommand("getplayersingame").setExecutor(new GetPlayersInGameCmdExec(this));
        this.getCommand("givediamondapple").setExecutor(new GiveDiamondAppleCmdExec(this));

        // register events listeners
        getServer().getPluginManager().registerEvents(new AppleListener(this),this);

    }

    public static UNCSurvival getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public MessageTchatManager getMessageTchatManager() {
        return messageTchatManager;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
