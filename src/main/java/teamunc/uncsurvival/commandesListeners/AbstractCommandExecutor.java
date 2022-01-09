package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.CommandExecutor;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;

abstract public class AbstractCommandExecutor implements CommandExecutor {
    protected final UNCSurvival plugin;
    protected final MessageTchatManager messageTchatManager;

    public AbstractCommandExecutor(UNCSurvival plugin) {
        this.plugin = plugin;
        this.messageTchatManager = this.plugin.getMessageTchatManager();
    }
}
