package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;

public class ReloadConfigurationCmdExec extends AbstractCommandExecutor {
    public ReloadConfigurationCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Reloading config");
        this.plugin.getGameManager().loadGameConfiguration();
        this.plugin.getGameManager().loadGameRuleConfiguration();
        return false;
    }
}
