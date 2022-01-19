package teamunc.uncsurvival.logic.gameStats;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.io.Serializable;

public class GameStats implements Serializable {
    private boolean gameStarted;
    private PhaseEnum actualPhase;

    public GameStats(boolean gameStarted, PhaseEnum actualPhase) {
        this.gameStarted = gameStarted;
        this.actualPhase = actualPhase;
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

    public PhaseEnum getActualPhase() {
        return actualPhase;
    }

    public void setActualPhase(PhaseEnum actualPhase) {
        this.actualPhase = actualPhase;
        this.saveStats();
    }
}
