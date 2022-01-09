package teamunc.uncsurvival.logic.manager;

import teamunc.uncsurvival.UNCSurvival;

public class AbstractManager {

    protected final UNCSurvival plugin;
    protected final MessageTchatManager messageTchatManager;

    public AbstractManager(UNCSurvival plugin) {
        this.plugin = plugin;
        this.messageTchatManager = this.plugin.getMessageTchatManager();
    }
}
