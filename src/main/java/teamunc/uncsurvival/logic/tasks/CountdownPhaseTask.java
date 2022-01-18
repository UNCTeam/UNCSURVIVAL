package teamunc.uncsurvival.logic.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseList;

import java.util.List;

public class CountdownPhaseTask extends BukkitRunnable {

    private UNCSurvival plugin =  UNCSurvival.getInstance();
    private PhaseList phase;
    private int secondes = 0;
    private int minutes;
    private int heures;
    private int jours;

    public CountdownPhaseTask(int minutes, int heures, int jours, PhaseList phase) {
        this.minutes = minutes;
        this.heures = heures;
        this.jours = jours;
        this.phase = phase;
    }

    public CountdownPhaseTask(PhaseList phase) {
        this.minutes = phase.getMinute();
        this.heures = phase.getHeure();
        this.jours = phase.getJour();
        this.phase = phase;
    }

    @Override
    public void run() {
        if(jours == 0 && heures == 0 && minutes == 0 && secondes == 0) {
            CountdownPhaseTask timerTask;
            switch (phase) {
                case PHASE1:
                    // 20 tic = 1s
                    timerTask = new CountdownPhaseTask(PhaseList.PHASE2);
                    timerTask.runTaskTimer(UNCSurvival.getInstance(),0, 20);
                    this.cancel();
                    break;
                case PHASE2:
                    timerTask = new CountdownPhaseTask(PhaseList.PHASE3);
                    timerTask.runTaskTimer(UNCSurvival.getInstance(),0, 20);
                    this.cancel();
                    break;
                case PHASE3:
                    // end game
                    break; }
        }
        if(heures == 0 && jours != 0) {
            this.jours--;
            this.heures = 24;
        }
        if(minutes == 0 && heures !=0) {
            this.heures--;
            this.minutes = 60;
        }
        if(secondes == 0 && minutes !=0) {
            this.minutes--;
            this.secondes = 60;
        }

        String tempsRestant = "§6§l- Temps restant : §b";
        if(jours == 0) {
            tempsRestant+=this.heures + " h " + this.minutes + " m " + this.secondes + " s";
        } else {
            tempsRestant+=this.jours + " j " + this.heures + " h " + this.minutes + " m ";
        }
        List<Player> players = plugin.getGameManager().getParticipantManager().getOnelinePlayers();
        for (Player player : players) {
            plugin.getGameManager().getScoreboardManager().getScoreboard(player).updateLine(4, tempsRestant);
        }
        secondes--;
    }
}
