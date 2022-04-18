package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Bukkit;
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
                    result.addAll(getTeamName(strings));
                } else if(strings.length == 2) {
                    List<String> players = Bukkit.getOnlinePlayers().stream()
                            .map(player -> player.getName())
                            .collect(Collectors.toList());
                    for (String name : players) {
                        if (name.toLowerCase().startsWith(strings[1].toLowerCase())) {
                            result.add(name);
                        }
                    }
                }
                break;
            case "removebonusscore":
            case "addbonusscore":
            case "statsteam":
                if(strings.length == 1) {
                    result.addAll(getTeamName(strings));
                }
                break;
            case "removetime":
            case "addtime":
                if (strings.length == 1) {
                    result.add("jours");
                    result.add("heures");
                    result.add("minutes");
                    result.add("secondes");
                    return result;
                }
                break;
        }
        return result;
    }

    public List<String> getTeamName(String[] strings) {
        List<String> result = new ArrayList<>();
        List<String> teams = plugin.getGameManager().getTeamsManager().getAllTeams().stream()
                .map(team -> team.getChatColor().name()).collect(Collectors.toList());
        for (String team : teams) {
            if (team.toLowerCase().startsWith(strings[0].toLowerCase())) {
                result.add(team);
            }
        }
        return result;
    }
}
