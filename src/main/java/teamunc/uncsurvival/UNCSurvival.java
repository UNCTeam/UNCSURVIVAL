package teamunc.uncsurvival;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.uncsurvival.commandesListeners.*;
import teamunc.uncsurvival.eventsListeners.EventsManager;
import teamunc.uncsurvival.logic.manager.*;
import teamunc.uncsurvival.logic.manager.FileManager;

public class UNCSurvival extends JavaPlugin {

    private GameManager gameManager;
    private TabManager tabManager;
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
        this.tabManager = new TabManager(this);
        tabManager.showTab();

        // post load
        this.postLoad();

        // register commands
        this.getCommand("startuncsurvival").setExecutor(new StartCmdExec(this));
        this.getCommand("getplayersingame").setExecutor(new GetPlayersInGameCmdExec(this));
        this.getCommand("givecustomitem").setExecutor(new GiveCustomItemsCmdExec(this));
        this.getCommand("givecustomitem").setTabCompleter(new CustomTabComplete(this));
        this.getCommand("addplayertoteam").setExecutor(new TeamCmdExec(this));
        this.getCommand("addplayertoteam").setTabCompleter(new CustomTabComplete(this));
        this.getCommand("removeplayertoteam").setExecutor(new TeamCmdExec(this));
        this.getCommand("removeteam").setExecutor(new TeamCmdExec(this));
        this.getCommand("addteam").setExecutor(new TeamCmdExec(this));

        this.getCommand("reloadconfig").setExecutor(new ReloadConfigurationCmdExec(this));
    }

    /**
     * fonction pour chargé les save apres le chargements complet des managers
     */
    private void postLoad() {
        // register des interface teams
        this.gameManager.getTeamsManager().getAllTeams().forEach(team -> {
            team.registerInterfaces();
        });
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
