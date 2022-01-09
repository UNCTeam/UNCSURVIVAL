package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamsManager extends AbstractManager {
    private final List<Team> teams;

    public TeamsManager(UNCSurvival plugin) {
        super(plugin);
        this.teams = new ArrayList<>();
        this.init();
    }

    public void init() {
        Team team1 = new Team("Rouge", ChatColor.RED, new Location(Bukkit.getWorld("world"), 0, 100, 0));
        Team team2 = new Team("Bleu", ChatColor.BLUE, new Location(Bukkit.getWorld("world"), 50, 100, 0));
        Team team3 = new Team("Jaune", ChatColor.YELLOW, new Location(Bukkit.getWorld("world"), 0, 100, 50));
        Team team4 = new Team("Verte", ChatColor.GREEN, new Location(Bukkit.getWorld("world"), 50, 100, 50));
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        teams.add(team4);
    }

    public void loadTeams() {

    }

    public void saveTeams() {
        final Gson gson = new Gson();
        try {
            gson.toJson(this, new FileWriter("/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
