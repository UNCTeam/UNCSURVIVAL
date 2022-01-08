package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.CommandExecutor;
import teamunc.uncsurvival.UNCSurvival;

abstract public class abstractCommandExecutor implements CommandExecutor {
    protected final UNCSurvival plugin;

    public abstractCommandExecutor(UNCSurvival plugin) {
        this.plugin = plugin;
    }
}
