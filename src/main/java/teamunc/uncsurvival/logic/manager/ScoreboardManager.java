package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.utils.scoreboards.VScoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private Map<UUID, VScoreboard> scoreboardMap;

    public ScoreboardManager() {
        this.scoreboardMap = new HashMap<>();
    }

    public void addScoreboard(VScoreboard vScoreboard){
        this.scoreboardMap.put(vScoreboard.getPlayer().getUniqueId(), vScoreboard);
    }

    public void update(int line,String value){
        getScoreboardMap().forEach((uuid, vScoreboard) -> vScoreboard.updateLine(line,value));
    }

    public void removeScoreboard(Player player){
        if(getScoreboard(player) == null) return;
        VScoreboard scoreboard = getScoreboard(player);
        scoreboard.delete();
        this.scoreboardMap.remove(player.getUniqueId());
    }

    public void removeAll(){
        for (UUID uuid : scoreboardMap.keySet()){
            removeScoreboard(Bukkit.getPlayer(uuid));
        }
    }

    public VScoreboard getScoreboard(Player player){
        return this.scoreboardMap.get(player.getUniqueId());
    }

    public Map<UUID, VScoreboard> getScoreboardMap() {
        return scoreboardMap;
    }

    public void setScoreboardMap(Map<UUID, VScoreboard> scoreboardMap) {
        this.scoreboardMap = scoreboardMap;
    }
}
