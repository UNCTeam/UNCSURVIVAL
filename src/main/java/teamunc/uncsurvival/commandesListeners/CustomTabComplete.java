package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import teamunc.uncsurvival.UNCSurvival;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CustomTabComplete implements TabCompleter {

    private UNCSurvival plugin;

    public CustomTabComplete(UNCSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> result = new ArrayList<>();
        switch(s) {
            case "givecustomitem":
                if (strings.length == 1) {
                    for (String a : plugin.getGameManager().getItemsManager().getCustomItems()) {
                        if (a.toLowerCase().startsWith(strings[0].toLowerCase())) {
                            result.add(a);
                        }
                    }
                    return result;
                }
                break;
            case "addplayertoteam":
                if (strings.length == 1) {
                    List<String> teams = plugin.getGameManager().getTeamsManager().getAllTeams().stream()
                            .map(team -> team.getName()).collect(Collectors.toList());
                    for (String team : teams) {
                        if (team.toLowerCase().startsWith(strings[0].toLowerCase())) {
                            result.add(team);
                        }
                    }
                }
                break;
        }
        return result;
    }
}
