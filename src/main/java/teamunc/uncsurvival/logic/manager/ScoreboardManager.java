package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.scoreboards.VScoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager extends AbstractManager{

    private Map<UUID, VScoreboard> scoreboardMap;
    private Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public ScoreboardManager(UNCSurvival plugin) {
        super(plugin);
        this.scoreboardMap = new HashMap<>();
    }

    public void addScoreboard(VScoreboard vScoreboard){
        this.scoreboardMap.put(vScoreboard.getPlayer().getUniqueId(), vScoreboard);
    }

    public void update(){
        getScoreboardMap().forEach((uuid, vScoreboard) -> vScoreboard.actualise());
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

    public void resetStatsScoreboard() {
        for(GamePlayer gamePlayer : plugin.getGameManager().getParticipantManager().getGamePlayers()) {
            this.scoreboard.resetScores(gamePlayer.getOfflinePlayer());
        }
    }

    public void initStatsScoreboard() {
        try {
            resetStatsScoreboard();
            this.scoreboard.registerNewObjective("STATS.MSTONE","minecraft.mined:minecraft.stone","TOTAL STONE MINED");
            this.scoreboard.registerNewObjective("STATS.PKILL","minecraft.custom:minecraft.player_kills","TOTAL PLAYERS KILLS");
            this.scoreboard.registerNewObjective("STATS.MKILL","minecraft.custom:minecraft.mob_kills","TOTAL MOBS KILLS");
            this.scoreboard.registerNewObjective("STATS.TPLAYED","minecraft.custom:minecraft.play_time","TOTAL TIME PLAYED");
            this.scoreboard.registerNewObjective("STATS.DEATH","deathCount","TOTAL DEATHS");
        } catch (IllegalArgumentException e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + e.toString());
        }
    }

    public int getDeathStats(OfflinePlayer player) {
        int res = 0;
        if (this.scoreboard.getObjective("STATS.DEATH") != null)
            res = this.scoreboard.getObjective("STATS.DEATH").getScore(player.getName()).getScore();
        return res;
    }

    public int getPlayerKill(OfflinePlayer player) {
        int res = 0;
        if (this.scoreboard.getObjective("STATS.PKILL") != null)
            res = this.scoreboard.getObjective("STATS.PKILL").getScore(player.getName()).getScore();
        return res;
    }

    public int getMobKill(OfflinePlayer player) {
        int res = 0;
        if (this.scoreboard.getObjective("STATS.MKILL") != null)
            res = this.scoreboard.getObjective("STATS.MKILL").getScore(player.getName()).getScore();
        return res;
    }

    public int getStoneMined(OfflinePlayer player) {
        int res = 0;
        if (this.scoreboard.getObjective("STATS.MSTONE") != null)
            res = this.scoreboard.getObjective("STATS.MSTONE").getScore(player.getName()).getScore();
        return res;
    }

    public int getTimePlayed(OfflinePlayer player) {
        int res = 0;
        if (this.scoreboard.getObjective("STATS.TPLAYED") != null)
            res = this.scoreboard.getObjective("STATS.TPLAYED").getScore(player.getName()).getScore();
        return res;
    }

}
