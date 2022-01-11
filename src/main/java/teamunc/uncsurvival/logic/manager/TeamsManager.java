package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamsManager extends AbstractManager implements Serializable {
    private final ArrayList<Team> teams;

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
        //saveTeams();
    }

    public void loadTeams() {

    }

    public ArrayList<Team> getAllTeams() {
        return this.teams;
    }

    public void saveTeams() {
        final Gson gson = new Gson();
        try {
            File file = new File(UNCSurvival.getInstance().getDataFolder().getAbsolutePath() + "/teams.json");
            file.getParentFile().mkdir();
            file.createNewFile();
            Writer writer = new FileWriter(file, false);
            gson.toJson(this.teams, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Team getTeam(String name) {
        Team res = null;
        Optional<Team> resOptional = this.teams.stream().filter(team -> team.getName() == name).findFirst();
        if (resOptional.isPresent()) {
            res = resOptional.get();
        }
        return res;
    }

    public Team getTeam(ChatColor color) {
        Team res = null;
        Optional<Team> resOptional = this.teams.stream().filter(team -> team.getChatColor() == color).findFirst();
        if (resOptional.isPresent()) {
            res = resOptional.get();
        }
        return res;
    }

}
