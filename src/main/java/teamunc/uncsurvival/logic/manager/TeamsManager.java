package teamunc.uncsurvival.logic.manager;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

public class TeamsManager extends AbstractManager implements Serializable {
    private TeamList teams;

    public TeamsManager(UNCSurvival plugin) {
        super(plugin);
        this.init();
    }

    public void init() {
        // loading teamList
        this.teams = this.plugin.getFileManager().loadTeams();
    }

    public ArrayList<Team> getAllTeams() {
        return this.teams.getTeams();
    }

    public ArrayList<Team> getClassement() {
        ArrayList<Team> classement = getAllTeams();
        Collections.sort(classement, Comparator.comparing(Team::getScore).reversed());
        return classement;
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
                if (p.getBukkitPlayer() != null) {
                    p.getBukkitPlayer().teleport(t.getSpawnPoint());
                    p.getBukkitPlayer().setBedSpawnLocation(t.getSpawnPoint(), true);
                }
            }
        }
    }
}
