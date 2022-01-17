package teamunc.uncsurvival.logic.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;

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

    public void actionsEachSeconds() {
        // place all events that can occur each seconds
        plugin.getGameManager().getScoreboardManager().update();

        // Actualise Water Level Display
        ThirstActualiser.getInstance().actualiseDisplay();

        // Check items
        this.plugin.getGameManager().getTeamsManager().getAllTeams().forEach(team -> {
            team.ConsumeAllGoalItems();
        });

        // damage due to Water
        if (this.secondes%5 == 0) ThirstActualiser.getInstance().damageAllnoWater();
    }

    public void actionsEachMinutes() {
        // place all events that can occur each minutes

        // dicrease Water Level of 1
        ThirstActualiser.getInstance().decreaseWaterForAllRegisteredPlayers(1);
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
