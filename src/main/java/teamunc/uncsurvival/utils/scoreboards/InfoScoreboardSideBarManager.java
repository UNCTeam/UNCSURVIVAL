package teamunc.uncsurvival.utils.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class InfoScoreboardSideBarManager {

    //# SINGLETON
    private static InfoScoreboardSideBarManager instance;
    private InfoScoreboardSideBarManager() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Objective creation
        if (scoreboard.getObjective("GAMEINFO") != null) scoreboard.getObjective("GAMEINFO").unregister();
        this.objective =
                scoreboard.registerNewObjective(
                        "GAMEINFO",
                        "dummy",
                        ChatColor.AQUA + "UNC SURVIVAL"
                );

        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.initDisplay();

    }
    public static InfoScoreboardSideBarManager getInstance() {
        if (InfoScoreboardSideBarManager.instance == null) InfoScoreboardSideBarManager.instance = new InfoScoreboardSideBarManager();
        return InfoScoreboardSideBarManager.instance;
    }
    //# END SINGLETON

    private Objective objective;
    private int actualSec = 0; // only used to change the seconds displayed
    private int actualMin = 0; // only used to change the minutes displayed
    private int actualPhase = 1; // only used to change the phase displayed

    public void initDisplay() {
        Score separator1Score = this.objective.getScore(ChatColor.GRAY + "-----------");
        Score secMinScore = this.objective.getScore(ChatColor.GREEN + " Time : " + ChatColor.AQUA + "00" + ChatColor.GREEN + " : " + ChatColor.AQUA + "00");
        Score phasesScore = this.objective.getScore(ChatColor.GREEN + " Phase : " + ChatColor.AQUA + "1");
        Score separator2Score = this.objective.getScore(ChatColor.DARK_GRAY + " ");
        Score separator3Score = this.objective.getScore(ChatColor.GRAY + "" + ChatColor.GRAY + "-----------");

        separator1Score.setScore(-1);
        secMinScore.setScore(-2);
        phasesScore.setScore(-3);
        separator2Score.setScore(-4);
        separator3Score.setScore(-5);
    }

    public void setTime(int seconds, int minutes) {
        // deleting old score
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.resetScores(ChatColor.GREEN + " Time : " + ChatColor.AQUA + String.format("%02d",this.actualMin) + ChatColor.GREEN + " : " + ChatColor.AQUA + String.format("%02d",this.actualSec));

        // creating new score
        Score secMinScore = this.objective.getScore(ChatColor.GREEN + " Time : " + ChatColor.AQUA + String.format("%02d",minutes) + ChatColor.GREEN + " : " + ChatColor.AQUA + String.format("%02d",seconds));
        secMinScore.setScore(-2);

        // storing these new score
        this.actualSec = seconds;
        this.actualMin = minutes;
    }

    public void setPhase(int newPhase) {
        // deleting old score
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        scoreboard.resetScores(ChatColor.GREEN + " Phase : " + ChatColor.AQUA + this.actualPhase);

        // creating new score
        Score phasesScore = this.objective.getScore(ChatColor.GREEN + " Phase : " + ChatColor.AQUA + newPhase);
        phasesScore.setScore(-3);

        // storing this new score
        this.actualPhase = newPhase;
    }

}
