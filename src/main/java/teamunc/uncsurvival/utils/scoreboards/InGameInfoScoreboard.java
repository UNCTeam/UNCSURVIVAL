package teamunc.uncsurvival.utils.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameInfoScoreboard extends VScoreboard{

    /**
     * @param player the player the scoreboard is for
     */
    public InGameInfoScoreboard(Player player) {
        super(player, ChatColor.RED + "" + ChatColor.BOLD + "UNC SURVIVAL");
    }

    @Override
    public List<String> getLines() {
        ArrayList<String> lines = new ArrayList<>();

        lines.addAll(
                Arrays.asList(
                        ChatColor.BOLD + "" + ChatColor.GOLD + "-----------------",
                        " ",
                        ChatColor.BOLD + "" + ChatColor.GOLD +"- Phase Actuelle : " + ChatColor.AQUA + UNCSurvival.getInstance().getGameManager().getGameStats().getCurrentPhase().getNom(),
                        " ",
                        ChatColor.BOLD + "" + ChatColor.GOLD +"- Temps restant :"
                )
        );
        String tempsRestant = "  §b";
        GameManager gameManager =  UNCSurvival.getInstance().getGameManager();
        if(gameManager != null) {
            if(gameManager.getTimerTask() != null) {
                if(gameManager.getTimerTask().getJours() == 0) {
                    tempsRestant+=gameManager.getTimerTask().getHeures() + "h ";
                    tempsRestant+=gameManager.getTimerTask().getMinutes() + "m ";
                    tempsRestant+=gameManager.getTimerTask().getSecondes() + "s";
                } else {
                    tempsRestant+=gameManager.getTimerTask().getJours() + "j ";
                    tempsRestant+=gameManager.getTimerTask().getHeures() + "h ";
                    tempsRestant+=gameManager.getTimerTask().getMinutes() + "m";
                }
            }
        }

        lines.add(tempsRestant);

        // each teams
        for (Team t : UNCSurvival.getInstance().getGameManager().getTeamsManager().getAllTeams()) {
            lines.add(ChatColor.RED + "- " + t.getChatColor() + t.getName() + ChatColor.GOLD + " : " + ChatColor.AQUA + "" + ChatColor.BOLD + t.getScore());
        }

        lines.addAll(
                Arrays.asList(
                        " ",
                        ChatColor.BOLD + "" + ChatColor.GOLD + "-----------------"
                )
        );
        return lines;
    }

    @Override
    public void actualise() {
        this.updateLines(this.getLines());
    }
}
