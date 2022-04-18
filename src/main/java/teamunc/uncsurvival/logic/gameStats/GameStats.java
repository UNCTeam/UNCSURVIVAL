package teamunc.uncsurvival.logic.gameStats;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class GameStats implements Serializable {
    private boolean gameStarted;
    private PhaseEnum currentPhase;
    private HashMap<String, ArrayList<String>> advancementsNamePerTeamColor;

    public GameStats(boolean gameStarted, PhaseEnum currentPhase, HashMap<String, ArrayList<String>> advancementsNamePerTeamColor) {
        this.gameStarted = gameStarted;
        this.currentPhase = currentPhase;
        this.advancementsNamePerTeamColor = advancementsNamePerTeamColor;
    }

    private void saveStats() {
        UNCSurvival.getInstance().getFileManager().saveGameStats(this);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        this.saveStats();
    }

    public PhaseEnum getCurrentPhase() {
        return currentPhase;
    }

    public ArrayList<String> getAdvancementHalfPointed(String colorTeam) {
        if (!this.advancementsNamePerTeamColor.containsKey(colorTeam))
            this.advancementsNamePerTeamColor.put(colorTeam,new ArrayList<>());
        return this.advancementsNamePerTeamColor.get(colorTeam);
    }

    public void addAdvancementHalfPointed(String colorTeam, String advancementName) {
        if (!this.advancementsNamePerTeamColor.containsKey(colorTeam))
            this.advancementsNamePerTeamColor.put(colorTeam,new ArrayList<>());

        this.advancementsNamePerTeamColor.get(colorTeam).add(advancementName);
        this.saveStats();
    }

    public void setCurrentPhase(PhaseEnum currentPhase) {
        this.currentPhase = currentPhase;
        this.saveStats();
    }
}
