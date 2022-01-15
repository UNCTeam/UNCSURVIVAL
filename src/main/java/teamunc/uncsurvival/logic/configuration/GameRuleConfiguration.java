package teamunc.uncsurvival.logic.configuration;

import org.bukkit.GameRule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameRuleConfiguration implements Serializable {
    private Map<GameRule, Boolean> gamerules;

    public GameRuleConfiguration(HashMap<GameRule, Boolean> gamerules) {
        this.gamerules = gamerules;
    }

    public Map<GameRule, Boolean> getGamerules() {
        return gamerules;
    }

    public void setGamerules(HashMap<GameRule, Boolean> gamerules) {
        this.gamerules = gamerules;
    }
}
