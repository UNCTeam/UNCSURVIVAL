package teamunc.uncsurvival.logic.manager;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.tasks.CountdownPhaseTask;

public class TimeManager extends AbstractManager{

    public TimeManager(UNCSurvival plugin) {
        super(plugin);
    }

    private BukkitScheduler scheduler = plugin.getServer().getScheduler();

    private int secondes = 0;
    private int minutes = 0;
    private int heures = 0;
    private int jours = 0;

    // ID OF SCHEDULE
    private int eachSecondsTimerID;

    public void startTimer() {
        this.eachSecondsTimerID =
                scheduler.scheduleSyncRepeatingTask(
                        this.plugin,
                        () -> {
                            // seconds
                            this.secondes++;
                            this.actionsEachSeconds();

                            // minutes
                            if (this.secondes >= 60) {
                                this.secondes = 0;
                                this.minutes++;
                                this.actionsEachMinutes();
                            }

                            // heures
                            if (this.minutes >= 60) {
                                this.minutes = 0;
                                this.heures++;
                                this.actionsEachHours();
                            }

                            // jours
                            if (this.heures >= 24) {
                                this.heures = 0;
                                this.jours++;
                                this.actionsEachDays();
                            }
                        },
                        0L,
                        20L
                );
    }

    public void stopTimer() {
        this.scheduler.cancelTask(this.eachSecondsTimerID);
    }

    public void checkNewPhase() {
        CountdownPhaseTask timer = this.plugin.getGameManager().getTimerTask();
        if(timer != null) {
            if(timer.getJours() <= 0 && timer.getHeures() <= 0 && timer.getMinutes() <= 0 && timer.getSecondes() <= 0) {
                switch (plugin.getGameManager().getGameStats().getCurrentPhase()) {
                    case LANCEMENT:
                        timer.cancel();
                        plugin.getGameManager().start();
                        break;
                    case PHASE1:
                        timer.cancel();
                        plugin.getGameManager().startPhase2();
                        break;
                    case PHASE2:
                        timer.cancel();
                        plugin.getGameManager().startPhase3();
                        break;
                    case PHASE3:
                        timer.cancel();
                        plugin.getGameManager().startEnding();
                        break; }
            }
        }
    }

    public void actionsEachSeconds() {
        PhaseEnum phase = plugin.getGameManager().getGameStats().getCurrentPhase();
        // Vérifi si une phase est terminé
        checkNewPhase();

        // Update les scoreboards
        plugin.getGameManager().getScoreboardManager().update();

        // Actualise Water Level Display
        ThirstActualiser.getInstance().actualiseDisplay();



        // Check items
        this.plugin.getGameManager().getTeamsManager().getAllTeams().forEach(team -> {
            if (phase != PhaseEnum.FIN) team.ConsumeAllGoalItems();
        });

        // damage due to Water
        if (this.secondes%5 == 0) ThirstActualiser.getInstance().damageAllnoWater();

        if(plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.LANCEMENT) {
            for(Player player : this.plugin.getGameManager().getParticipantManager().getOnelinePlayers()) {
                if(plugin.getGameManager().getTimerTask().getSecondes() == 13) {
                    player.sendTitle("§bDébut du jeu dans : ", "", 20, 30, 20);
                } else if(plugin.getGameManager().getTimerTask().getSecondes() <= 10){
                    player.sendTitle(" §c§l" + plugin.getGameManager().getTimerTask().getSecondes(), "", 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,0.4F, 2);
                }
            }
        }
    }

    public void actionsEachMinutes() {
        // place all events that can occur each minutes

        // dicrease Water Level of 1
        if (this.minutes%2 == 0) ThirstActualiser.getInstance().decreaseWaterForAllRegisteredPlayers(1);
    }

    public void actionsEachHours() {
        // place all events that can occur each phases
    }

    public void actionsEachDays() {
        // place all events that can occur each phases
    }

    public int getSecondes() {
        return secondes;
    }
}
