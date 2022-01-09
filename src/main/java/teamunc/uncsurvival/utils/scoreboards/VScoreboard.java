package teamunc.uncsurvival.utils.scoreboards;

import org.bukkit.entity.Player;

import java.util.List;

public abstract class VScoreboard extends FastBoard {
    /**
     * Creates a new FastBoard.
     *
     * @param player the player the scoreboard is for
     */
    public VScoreboard(Player player,String title) {
        super(player);
        this.updateTitle(title);

        updateLines(getLines());
    }

    public abstract List<String> getLines();

    public abstract void actualise();
}
