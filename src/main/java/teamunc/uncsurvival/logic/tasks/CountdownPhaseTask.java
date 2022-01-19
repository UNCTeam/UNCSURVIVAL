package teamunc.uncsurvival.logic.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CountdownPhaseTask extends BukkitRunnable {

    private UNCSurvival plugin =  UNCSurvival.getInstance();
    private PhaseEnum phase;
    private int secondes = 0;
    private int minutes;
    private int heures;
    private int jours;

    public CountdownPhaseTask(int minutes, int heures, int jours) {
        this.minutes = minutes;
        this.heures = heures;
        this.jours = jours;
    }

    public CountdownPhaseTask(LocalDateTime date) {
        LocalDateTime today = LocalDateTime.now();
        this.jours = (int) ChronoUnit.DAYS.between(today, date);
        this.minutes = (int) ChronoUnit.MINUTES.between(today, date);
        this.heures = (int) ChronoUnit.HOURS.between(today, date);
        this.secondes = (int) ChronoUnit.SECONDS.between(today, date);
    }

    public CountdownPhaseTask(PhaseEnum phase) {
        this.minutes = phase.getMinute();
        this.heures = phase.getHeure();
        this.jours = phase.getJour();
        this.phase = phase;
    }

    @Override
    public void run() {
        if(heures <= 0 && jours != 0) {
            this.jours--;
            this.heures = 24;
        }
        if(minutes <= 0 && heures !=0) {
            this.heures--;
            this.minutes = 60;
        }
        if(secondes <= 0 && minutes !=0) {
            this.minutes--;
            this.secondes = 60;
        }

        secondes--;
    }

    public PhaseEnum getPhase() {
        return phase;
    }

    public void setPhase(PhaseEnum phase) {
        this.phase = phase;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHeures() {
        return heures;
    }

    public void setHeures(int heures) {
        this.heures = heures;
    }

    public int getJours() {
        return jours;
    }

    public void setJours(int jours) {
        this.jours = jours;
    }

    public void addJours(int jours) {
        this.jours = this.jours + jours;
    }

    public void addHeures(int heures) {
        this.heures = this.heures + heures;
    }

    public void addMinutes(int minutes) {
        this.minutes = this.minutes + minutes;
    }

    public int getSecondes() {
        return secondes;
    }

    public void setSecondes(int secondes) {
        this.secondes = secondes;
    }

    public void addSecondes(int secondes) {
        this.secondes = this.secondes + secondes;
    }

    public void addTime(int jours, int heures, int minutes, int secondes) {
        this.addJours(jours);
        this.addHeures(heures);
        this.addMinutes(minutes);
        this.addSecondes(secondes);
    }

    public void removeJours(int jours) {
        this.jours = this.jours - jours;
    }

    public void removeHeures(int heures) {
        this.heures = this.heures - heures;
    }

    public void removeMinutes(int minutes) {
        this.minutes = this.minutes - minutes;
    }

    public void removeSecondes(int secondes) {
        this.secondes = this.secondes - secondes;
    }

    public void removeTime(int jours, int heures, int minutes, int secondes) {
        this.removeJours(jours);
        this.removeHeures(heures);
        this.removeMinutes(minutes);
        this.removeSecondes(secondes);
    }
}
