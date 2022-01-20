package teamunc.uncsurvival.eventsListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.Team;

public class playerDeathListener extends AbstractEventsListener {
    public playerDeathListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        for (Team t : this.plugin.getGameManager().getTeamsManager().getAllTeams()) {
            if (this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p) != t) {
                t.addABonusScore(100);
            }
        }

        this.plugin.getMessageTchatManager().sendGeneralMesssage("");
    }
}
