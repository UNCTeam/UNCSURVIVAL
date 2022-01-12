package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;
import teamunc.uncsurvival.utils.FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeamsManager extends AbstractManager implements Serializable {
    private TeamList teams;

    public TeamsManager(UNCSurvival plugin) {
        super(plugin);
        this.init();
    }

    public void init() {
        // loading teamList
        TeamList teamListLoaded = FileService.loadTeams();
        if (teamListLoaded != null)
            this.teams = teamListLoaded;
        else {
            this.teams = new TeamList();
            FileService.saveTeams(this.teams);
        }
    }

    public ArrayList<Team> getAllTeams() {
        return this.teams.getTeams();
    }

    public Team addTeam(String name, ChatColor chatColor,Location spawnPoint) {
        Team t = null;
        if (!this.teams.hasTeam(name)) {
            t = new Team(name, chatColor, spawnPoint);
            this.teams.addTeam(t);
        }

        return t;
    }

    public Team removeTeam(String name) {
        Team team = null;
        for (Team t : this.teams.getTeams()) {
            if ( t.getName().equals(name) ) {
                team = t;
            }
        }
        this.teams.removeTeam(team);

        return team;
    }

    public void removeTeam(Team t) {
        this.teams.removeTeam(t);
    }

    public void removeTeam(ChatColor color) {
        for (Team t : this.teams.getTeams()) {
            if (t.getChatColor() == color) this.teams.removeTeam(t);
        }
    }

    public Team getTeam(String name) {
        return this.teams.getTeam(name);
    }

    public Team getTeam(ChatColor color) {
        Team res = null;
        Optional<Team> resOptional = this.teams.getTeams().stream().filter(team -> team.getChatColor() == color).findFirst();
        if (resOptional.isPresent()) {
            res = resOptional.get();
        }
        return res;
    }


    public void saveTeams() {
        FileService.saveTeams(this.teams);
    }

}
