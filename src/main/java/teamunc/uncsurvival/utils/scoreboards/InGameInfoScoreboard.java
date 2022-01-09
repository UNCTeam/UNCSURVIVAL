package teamunc.uncsurvival.utils.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;

import java.util.Arrays;
import java.util.List;

public class InGameInfoScoreboard extends VScoreboard{


    /**
     * @param player the player the scoreboard is for
     */
    public InGameInfoScoreboard(Player player) {
        super(player, ChatColor.BOLD + "UNC SURVIVAL");
    }

    @Override
    public List<String> getLines() {
        return Arrays.asList(
                "Seconds : " + UNCSurvival.getInstance().getGameManager().getTimeManager().getSecondes(),
                "Test2"
        );
    }

    @Override
    public void actualise() {
        this.updateLines(
                "Seconds : " + UNCSurvival.getInstance().getGameManager().getTimeManager().getSecondes(),
                "Test2");
    }
}
