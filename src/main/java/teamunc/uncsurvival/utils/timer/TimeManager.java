package teamunc.uncsurvival.utils.timer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.uncsurvival.UNCSURVIVAL;
import teamunc.uncsurvival.utils.scoreboards.InfoScoreboardSideBarManager;

import java.awt.event.ActionEvent;

public class TimeManager {

    //# SINGLETON
    private static TimeManager instance;
    private TimeManager() {}
    public static TimeManager getInstance() {
        if (TimeManager.instance == null) TimeManager.instance = new TimeManager();
        return TimeManager.instance;
    }
    //# END SINGLETON

    private JavaPlugin plugin = UNCSURVIVAL.getPlugin();
    private BukkitScheduler scheduler = plugin.getServer().getScheduler();

    private int secondes = 0;
    private int minutes = 0;
    /** each 20 minutes */
    private int phase = 1;

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

                            // phases
                            if (this.minutes >= 20) {
                                this.minutes = 0;
                                this.phase++;
                                this.actionsEachPhases();
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
        InfoScoreboardSideBarManager.getInstance().setTime(this.secondes,this.minutes);
    }

    public void actionsEachMinutes() {
        // place all events that can occur each minutes
        InfoScoreboardSideBarManager.getInstance().setTime(this.secondes,this.minutes);
    }

    public void actionsEachPhases() {
        // place all events that can occur each phases
        InfoScoreboardSideBarManager.getInstance().setPhase(this.phase);
    }

}
