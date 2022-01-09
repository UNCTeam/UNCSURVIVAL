package teamunc.uncsurvival.logic.manager;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.logic.team.TeamList;

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
        for(TeamList team : TeamList.values()) {
            this.teams.add(new Team(team.getName(),team.getChatColor()));
        }
    }

    public void addPlayerTeam

}
