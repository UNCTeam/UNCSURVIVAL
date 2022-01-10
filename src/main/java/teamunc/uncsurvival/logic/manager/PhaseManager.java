package teamunc.uncsurvival.logic.manager;

import teamunc.uncsurvival.UNCSurvival;

public class PhaseManager extends AbstractManager{
    private int phaseNumber = 1;

    public PhaseManager(UNCSurvival plugin) {
        super(plugin);
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    /**
     * Vient incrémenter le numero de la phase et lancer les nouveautés (qui s'execute une seul fois)
     * enclenché dans la commande /phaseSuivante
     * @return boolean si la commande est bien passé à la suite ou non
     */
    public boolean goToTheNewPhase() {
        // TODO penser au recharchegement du plugin pour la réexecution
        this.phaseNumber++;
        boolean res = true;

        switch (this.phaseNumber) {
            case 2:
                // TODO mettre les spécificités phase 2
                break;
            case 3:
                // TODO mettre les spécificités phase final
                break;
            default:
                // max, annulation
                this.phaseNumber--;
                res = false;
                break;
        }

        return res;
    }

    public void actionsSpecialInPhasesEachSeconds() {
        switch (this.phaseNumber) {
            case 2:
                // TODO mettre les spécificités phase 2
                break;
            case 3:
                // TODO mettre les spécificités phase final
                break;
        }
    }

}
