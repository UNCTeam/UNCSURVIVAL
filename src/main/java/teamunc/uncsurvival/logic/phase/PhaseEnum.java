package teamunc.uncsurvival.logic.phase;

public enum PhaseEnum {
    INIT("Waiting"),
    LANCEMENT("Lancement"),
    PHASE1("Phase 1"),
    PHASE2("Phase 2"),
    PHASE3("Phase 3"),
    FIN("Fin");

    private String nom;

    PhaseEnum(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
