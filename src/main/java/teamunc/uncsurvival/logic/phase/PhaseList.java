package teamunc.uncsurvival.logic.phase;

public enum PhaseList {
    PHASE1(3,0,0),
    PHASE2(3,0,0),
    PHASE3(3,0,0);

    private int jour;
    private int heure;
    private int minute;
    private PhaseList nextPhase;

    PhaseList(int jour, int heure, int minute) {
        this.jour = jour;
        this.heure = heure;
        this.minute = minute;
    }

    public int getJour() {
        return jour;
    }

    public int getHeure() {
        return heure;
    }

    public int getMinute() {
        return minute;
    }
}
