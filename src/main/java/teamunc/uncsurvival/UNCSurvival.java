package teamunc.uncsurvival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.commandesListeners.*;
import teamunc.uncsurvival.eventsListeners.EventsManager;
import teamunc.uncsurvival.logic.manager.*;
import teamunc.uncsurvival.logic.manager.FileManager;

public class UNCSurvival extends JavaPlugin {

    private GameManager gameManager;
    private MessageTchatManager messageTchatManager;
    private FileManager fileManager;
    private static UNCSurvival instance;
    private EventsManager eventsManager;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.broadcastMessage("UNCSurvival ON");

        // init File Manager
        this.fileManager = new FileManager(this);

        // init Managers
        this.messageTchatManager = new MessageTchatManager(this);
        this.gameManager = new GameManager(this);
        this.eventsManager = new EventsManager(this);

        // register commands
        this.getCommand("startuncsurvival").setExecutor(new StartCmdExec(this));
        this.getCommand("addplayer").setExecutor(new AddPlayerCmdExec(this));
        this.getCommand("removeplayer").setExecutor(new RemovePlayerCmdExec(this));
        this.getCommand("getplayersingame").setExecutor(new GetPlayersInGameCmdExec(this));
        this.getCommand("givediamondapple").setExecutor(new GiveDiamondAppleCmdExec(this));
        this.getCommand("addplayertoteam").setExecutor(new JoinTeamCmdExec(this));
        this.getCommand("removeplayertoteam").setExecutor(new LeaveTeamCmdExec(this));
        this.getCommand("removeteam").setExecutor(new RemoveTeamCmdExec(this));
        this.getCommand("addteam").setExecutor(new AddTeamCmdExec(this));

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

    public FileManager getFileManager() {
        return fileManager;
    }

    public EventsManager getEventsManager() {
        return this.eventsManager;
    }

    @Override
    public void onDisable() {
        // Save
        this.gameManager.save();
    }
}
