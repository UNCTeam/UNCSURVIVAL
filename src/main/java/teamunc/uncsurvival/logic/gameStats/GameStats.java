package teamunc.uncsurvival.logic.gameStats;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.io.Serializable;

public class GameStats implements Serializable {
    private boolean gameStarted;
    private PhaseEnum currentPhase;

    public GameStats(boolean gameStarted, PhaseEnum currentPhase) {
        this.gameStarted = gameStarted;
        this.currentPhase = currentPhase;
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

    public void setCurrentPhase(PhaseEnum currentPhase) {
        this.currentPhase = currentPhase;
        this.saveStats();
    }
}
