package teamunc.uncsurvival.logic.configuration;

import org.bukkit.GameRule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameRuleConfiguration implements Serializable {
    private Map<GameRule, Boolean> gamerules;

    public GameRuleConfiguration(HashMap<GameRule, Boolean> gamerules) {
        this.gamerules = gamerules;
    }

    @Override
    public String toString() {
        return "GameRuleConfiguration{" +
                "gamerules=" + gamerules +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameRuleConfiguration that = (GameRuleConfiguration) o;
        return Objects.equals(gamerules, that.gamerules);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gamerules);
    }

    public Map<GameRule, Boolean> getGamerules() {
        return gamerules;
    }

    public void setGamerules(HashMap<GameRule, Boolean> gamerules) {
        this.gamerules = gamerules;
    }
}
