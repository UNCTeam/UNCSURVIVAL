package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class TeamsManager extends AbstractManager implements Serializable {
    private TeamList teams;

    public TeamsManager(UNCSurvival plugin) {
        super(plugin);
        this.init();
    }

    public void init() {
        // loading teamList
        TeamList teamListLoaded = this.plugin.getFileManager().loadTeams();
        if (teamListLoaded != null)
            this.teams = teamListLoaded;
        else {
            this.teams = new TeamList();
            this.plugin.getFileManager().saveTeams(this.teams);
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
        this.plugin.getFileManager().saveTeams(this.teams);
    }

    public void teleportEveryPlayers() {
        for (Team t : this.getAllTeams()) {
            for (GamePlayer p : t.getMembers()) {
                p.getBukkitPlayer().teleport(t.getSpawnPoint());
                p.getBukkitPlayer().setBedSpawnLocation(t.getSpawnPoint(),true);
            }
        }
    }
}
