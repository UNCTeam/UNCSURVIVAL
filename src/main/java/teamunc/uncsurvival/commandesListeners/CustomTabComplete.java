package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import teamunc.uncsurvival.UNCSurvival;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomTabComplete implements TabCompleter {

    private UNCSurvival plugin;

    public CustomTabComplete(UNCSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        List<String> result = new ArrayList<>();
        if (strings.length == 1) {
            for (String a : plugin.getGameManager().getItemsManager().getCustomItems()) {
                if (a.toLowerCase().startsWith(strings[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }
        return null;
    }
}
