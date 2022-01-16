package teamunc.uncsurvival.logic.gameStats;

import teamunc.uncsurvival.UNCSurvival;

import java.io.Serializable;

public class GameStats implements Serializable {
    private boolean gameStarted;
    private int actualPhase;

    public GameStats(boolean gameStarted, int actualPhase) {
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

    public int getActualPhase() {
        return actualPhase;
    }

    public void setActualPhase(int actualPhase) {
        this.actualPhase = actualPhase;
        this.saveStats();
    }
}
