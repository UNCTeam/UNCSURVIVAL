package teamunc.uncsurvival.logic.team;

import java.io.Serializable;
import java.util.ArrayList;

public class TeamList implements Serializable {
    private static transient final long serialVersionUID = -1681012206529286330L;
    private ArrayList<Team> teams = new ArrayList<>();

    public TeamList() {
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public void removeTeam(Team team) {
        this.teams.remove(team);
    }

    public Team getTeam(String name) {
        Team t = null;

        for (Team team : this.teams) {
            if ( team.getName().equals(name) ) t = team;
        }

        return t;
    }

    public boolean hasTeam(String name) {
        return (this.getTeam(name) != null);
    }


}
