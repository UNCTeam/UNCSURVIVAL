package teamunc.uncsurvival.logic.advancements;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import teamunc.uncsurvival.logic.team.Team;

import java.io.Serializable;

public abstract class Advancement implements Serializable {
    protected String key;
    protected ChatColor teamColor = null;

    public Advancement(String key) {
        this.key = key;
    }

    public boolean alreadyGranted() {
        return (this.teamColor != null);
    }

    public abstract int givenPoints();
    public abstract String DisplayedName();

    public NamespacedKey getKey() {
        return new NamespacedKey("uncsurvival",key);
    }

    public void setWinningTeam(Team team) {
        this.teamColor = team.getChatColor();
    }

    public ChatColor getTeamColor() {
        return teamColor;
    }
}
